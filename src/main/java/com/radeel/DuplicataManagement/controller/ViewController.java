package com.radeel.DuplicataManagement.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.radeel.DuplicataManagement.model.Admin;
import com.radeel.DuplicataManagement.model.Client;
import com.radeel.DuplicataManagement.service.ElectricityDuplicataService;
import com.radeel.DuplicataManagement.service.UserManager;
import com.radeel.DuplicataManagement.util.Point;
import com.radeel.DuplicataManagement.util.UserRegister;
import com.radeel.DuplicataManagement.util.UserResponse;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Controller
@Validated
@Transactional
public class ViewController {
  
  @Autowired
  private UserManager userManager;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private ElectricityDuplicataService duplicataService;

  @ExceptionHandler({
    IllegalStateException.class,
    ConstraintViolationException.class,
    MethodArgumentNotValidException.class,
    MissingServletRequestParameterException.class,
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody String Error(Exception exception){
    String err = "";
    if(exception instanceof MethodArgumentNotValidException){
      var ex = (MethodArgumentNotValidException)exception;
      String errors = "";
      for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
        errors += error.getField() + error.getDefaultMessage() + '\n';
      }
      err += String.join("\n",errors);
    }
    if(exception instanceof ConstraintViolationException){
      var ex = (ConstraintViolationException)exception;
      String errors = "";
      for(final var cv : ex.getConstraintViolations()){
        errors += cv.getMessage() + '\n';
      }
      err += errors;
    }
    if(exception instanceof MissingServletRequestParameterException){
      err += ((MissingServletRequestParameterException)exception).getParameterName() + " parameter is missing !";
    }
    if(exception instanceof IllegalStateException){
      err += ((IllegalStateException)exception).getMessage();
    }
    return err;
  }

  @ModelAttribute("isAdmin")
  public boolean isAdmin(@AuthenticationPrincipal Admin admin){
    return admin != null;
  }

  @ModelAttribute("logged_in")
  public boolean isLoggedIn(@AuthenticationPrincipal UserDetails user){
    return user != null;
  }


  @GetMapping("/navbar")
  public String NavBar(){
    return "navbar";
  }

  @GetMapping(value = {"/","/login","/logout"})
  public String Login(){
    return "login";
  }

  @GetMapping("/main")
  public String Main(@AuthenticationPrincipal UserDetails user){
    if(user instanceof Client){
      return ((Client)user).isActive() ? "redirect:/export" : "redirect:/verify_client";
    } 
    if(user instanceof Admin) return "redirect:/list_users";
    return "redirect:/login";
  }

  @GetMapping("/create_admin")
  public String createAdmin(){
    return "create_admin";
  }

  @PostMapping("/create_admin")
  @ResponseBody public String addAdmin(
    @Valid UserRegister adminRegister
  ){
    Admin admin = new Admin(
      0,
      adminRegister.getEmail(),
      adminRegister.getUsername(),
      passwordEncoder.encode(adminRegister.getPassword()),
      new ArrayList<>()
    );
    userManager.addAdmin(admin);
    return "list_users";
  }

  @GetMapping("/create_client")
  public String createClient(){
    return "create_client";
  }

  @PostMapping("/create_client")
  @ResponseBody public String addClient(
    @Valid UserRegister clientRegister,
    @Valid @NotBlank(message = "category must not be blank !") String category
  ){
    var cat = userManager.addClientCategory(category);
    Client client = new Client(
      0,
      true,
      clientRegister.getEmail(),
      clientRegister.getUsername(),
      passwordEncoder.encode(clientRegister.getPassword()),
      cat,
      new ArrayList<>(),
      new ArrayList<>(),
      new ArrayList<>()
    );
    cat.getClients().add(client);
    userManager.saveClientCategory(cat);
    userManager.addClient(client);
    return "list_users";
  }

  @GetMapping("/verify_client")
  public String verifyClient(@AuthenticationPrincipal Client client){
    if(client == null || client.isActive()) return "redirect:/login";
    return "verify_client";
  }

  @PostMapping("/verify_client")
  @ResponseBody
  public String verify(
    @AuthenticationPrincipal Client client,
    @Valid @Email @NotBlank(message = "email must not be blanck") String email,
    @Valid @Length(min = 8,message = "password must be at least 8 characters") String password
  ) throws MessagingException{
    if(client != null){
      client.setEmail(email);
      client.setPassword(passwordEncoder.encode(password));
      userManager.addVerficationLink(client);
    }
    return "login";
  }

  @GetMapping("/confirm_client")
  public String confirm(@RequestParam String link){
    userManager.verifyClient(link);
    return "redirect:/login";
  }

  @GetMapping("/list_users")
  public String listUsers(Model model){
    model.addAttribute("users", userManager.getAllUsers());
    return "list_users";
  }

  @GetMapping("/modify_client")
  public String modifyClient(
    Model model,
    @Valid long id
  ){
    var user = userManager.getClientById(id);
    model.addAttribute("user",UserResponse.fromClient(user));
    model.addAttribute("epols",duplicataService.getElectricityPolicesByClient(user));
    model.addAttribute("wpols",duplicataService.getWaterPolicesByClient(user));
    return "modify_client";
  }

  @GetMapping("/modify_account")
  public String modifyAccount(
    @AuthenticationPrincipal Client client,
    Model model
  ){
    model.addAttribute("user",UserResponse.fromClient(client));
    return "modify_account";
  }

  @PostMapping("/modify_client")
  @ResponseBody
  public String changeClient(
    @Valid @RequestParam long id,
    @Valid @Email @NotBlank(message = "email must not be blank") String email,
    @Valid @NotBlank(message = "username must not be blank") String username,
    @Valid @NotBlank(message = "category must not be blank") String category,
    String check,
    @RequestParam(required = false) String password,
    String list1,
    String list2
  ) throws JsonMappingException, JsonProcessingException{
    Client client = userManager.getClientById(id);
    if(check != null){
      if(password == null || password.length() < 8){
        throw new IllegalStateException("The password should contain at least 8 characters !");
      }
      client.setPassword(passwordEncoder.encode(password));
    }
    client.setEmail(email);
    client.setUsername(username);
    var cat = userManager.addClientCategory(category);
    client.setCategory(cat);
    cat.getClients().add(client);
    userManager.saveClientCategory(cat);
    ObjectMapper objectMapper = new ObjectMapper();
    List<Point> ele = objectMapper.readValue(list1,new TypeReference<List<Point>>(){}); 
    List<Point> wat = objectMapper.readValue(list2,new TypeReference<List<Point>>(){}); 
    duplicataService.setPolices(client,ele,wat);
    return "list_users";
  }

  @PostMapping("/modify_account")
  @ResponseBody
  public String changeAccount(
    @AuthenticationPrincipal Client client,
    @Valid @Email @NotBlank(message = "email must not be blank") String email,
    @Valid @NotBlank(message = "username must not be blank") String username,
    @Valid @NotBlank(message = "category must not be blank") String category,
    String check,
    @RequestParam(required = false) String password
  ){
    if(check != null){
      if(password == null || password.length() < 8){
        throw new IllegalStateException("The password should contain at least 8 characters !");
      }
      client.setPassword(passwordEncoder.encode(password));
    }
    client.setEmail(email);
    client.setUsername(username);
    var cat = userManager.addClientCategory(category);
    client.setCategory(cat);
    cat.getClients().add(client);
    userManager.saveClientCategory(cat);
    return "modify_account";
  }

  @GetMapping("/delete_client")
  public String deleteClient(RedirectAttributes model,@Valid long id){
    if(!userManager.deleteClient(id)){
      model.addAttribute("error", true);
    }
    return "redirect:/list_users";
  }

  @GetMapping("/modify_admin")
  public String modifyAdmin(
    Model model,
    @Valid long id
  ){
    model.addAttribute("user",UserResponse.fromAdmin(userManager.getAdminById(id)));
    return "modify_admin";
  }

  @PostMapping("/modify_admin")
  @ResponseBody
  public String changeAdmin(
    @Valid @RequestParam long id,
    @Valid @Email @NotBlank(message = "email must not be blank") String email,
    @Valid @NotBlank(message = "username must not be blank") String username,
    String check,
    @RequestParam(required = false) String password
  ){
    Admin admin = userManager.getAdminById(id);
    if(check != null){
      if(password == null || password.length() < 8){
        throw new IllegalStateException("The password should contain at least 8 characters !");
      }
      admin.setPassword(passwordEncoder.encode(password));
    }
    admin.setEmail(email);
    admin.setUsername(username);
    userManager.saveAdmin(admin);
    return "list_users";
  }

  @GetMapping("/delete_admin")
  public String deleteAdmin(RedirectAttributes model,@Valid long id){
    if(!userManager.deleteAdmin(id)){
      model.addAttribute("error", true);
    }
    return "redirect:/list_users";
  }
}
