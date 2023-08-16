package com.radeel.DuplicataManagement.controller;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.radeel.DuplicataManagement.model.Admin;
import com.radeel.DuplicataManagement.model.Client;
import com.radeel.DuplicataManagement.model.Role;
import com.radeel.DuplicataManagement.service.UserManager;
import com.radeel.DuplicataManagement.util.UserRegister;
import com.radeel.DuplicataManagement.util.UserResponse;

import jakarta.mail.MessagingException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Controller
@Validated
public class View {
  
  @Autowired
  private UserManager userManager;

  @Autowired
  private PasswordEncoder passwordEncoder;


  @ExceptionHandler({
    IllegalStateException.class,
    ConstraintViolationException.class,
    MethodArgumentNotValidException.class,
    MissingServletRequestParameterException.class
  })
  @ModelAttribute("error")
  public String Error(Exception exception){
    if(exception instanceof MethodArgumentNotValidException){
      var ex = (MethodArgumentNotValidException)exception;
      String errors = "";
      for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
        errors += error.getField() + error.getDefaultMessage() + '\n';
      }
      return String.join("\n",errors);
    }
    if(exception instanceof ConstraintViolationException){
      var ex = (ConstraintViolationException)exception;
      String errors = "";
      for(final var cv : ex.getConstraintViolations()){
        errors += cv.getMessage() + '\n';
      }
      return errors;
    }
    if(exception instanceof MissingServletRequestParameterException){
      return ((MissingServletRequestParameterException)exception).getParameterName() + " parameter is missing !";
    }
    return exception.getMessage();
  }


  @GetMapping("/navbar")
  public String NavBar(){
    return "navbar";
  }

  @GetMapping(value = {"/","/login"})
  public String Login(){
    return "login";
  }

  @GetMapping("/create_admin")
  public String createAdmin(){
    return "create_admin";
  }

  @PostMapping("/create_admin")
  public String addAdmin(
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
    return "create_admin";
  }

  @GetMapping("/create_client")
  public String createClient(){
    return "create_client";
  }

  @PostMapping("/create_client")
  public String addClient(
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
      new ArrayList<>()
    );
    cat.getClients().add(client);
    userManager.saveClientCategory(cat);
    userManager.addClient(client);
    return "create_client";
  }

  @GetMapping("/verify_client")
  public String verifyClient(@AuthenticationPrincipal Client client){
    if(client == null || client.isActive()) return "redirect:/login";
    return "verify_client";
  }

  @PostMapping("/verify_client")
  public String verify(
    @AuthenticationPrincipal Client client,
    @Valid @NotBlank(message = "email must not be blanck") String email,
    @Valid @Length(min = 8,message = "password must be at least 8 characters") String password
  ) throws MessagingException{
    if(client != null){
      client.setEmail(email);
      client.setPassword(passwordEncoder.encode(password));
      userManager.addVerficationLink(client);
      userManager.saveClient(client);
    }
    return "verify_client";
  }

  @GetMapping("/confirm_client?link=")
  public String confirm(@RequestParam String link){
    userManager.verifyClient(link);
    return "login";
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
    model.addAttribute("user",UserResponse.fromClient(userManager.getClientById(id)));
    return "modify_client";
  }

  @PostMapping("/modify_client")
  public String changeClient(
    @Valid @RequestParam long id,
    @Valid @NotBlank(message = "email must not be blank") String email,
    @Valid @NotBlank(message = "username must not be blank") String username,
    @Valid @NotBlank(message = "category must not be blank") String category,
    String check,
    @RequestParam(required = false) String password
  ){
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
    return "redirect:/list_users";
  }

  @GetMapping("/delete_client")
  public String deleteClient(@Valid long id){
    userManager.deleteClient(id);
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
  public String changeAdmin(
    @Valid @RequestParam long id,
    @Valid @NotBlank(message = "email must not be blank") String email,
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
    return "redirect:/list_users";
  }

  @GetMapping("/delete_admin")
  public String deleteAdmin(@Valid long id){
    userManager.deleteAdmin(id);
    return "redirect:/list_users";
  }
}
