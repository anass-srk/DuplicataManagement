package com.radeel.DuplicataManagement.controller;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.radeel.DuplicataManagement.model.Admin;
import com.radeel.DuplicataManagement.model.Client;
import com.radeel.DuplicataManagement.model.Gerance;
import com.radeel.DuplicataManagement.model.RequestState;
import com.radeel.DuplicataManagement.service.RequestService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;

@Controller
@Validated
@Transactional
public class RequestController {

  @Autowired 
  private RequestService service;

  @ExceptionHandler({
    IllegalStateException.class,
    ConstraintViolationException.class,
    MethodArgumentNotValidException.class,
    MissingServletRequestParameterException.class,
    MethodArgumentTypeMismatchException.class
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
    if(exception instanceof MethodArgumentTypeMismatchException){
      err += "Invalid localite and gerance and police !";
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

  @GetMapping("/list_requests")
  public String listRequests(Model model){
    var requests = service.getAllRequests();
    model.addAttribute("requests", requests);
    return "list_requests";
  }

  @GetMapping("/answer_request")
  public String answerRequest(
    @AuthenticationPrincipal Admin admin,
    @Valid long id,
    @Valid RequestState status
  ){
    if(!service.answerRequest(admin,id, status)){
      return "list_requests?error";
    }
    return "list_requests";
  }

  @GetMapping("/client_requests")
  public String listClientRequests(
    Model model,
    @AuthenticationPrincipal Client client
    ){
    var requests = service.getClientRequests(client);
    model.addAttribute("requests", requests);
    return "list_requests";
  }

  @GetMapping("/create_request")
  public String createRequest(){
    return "create_request";
  }

  @PostMapping("/create_request")
  @ResponseBody
  public String addRequest(
    @AuthenticationPrincipal Client client,
    @Valid short localite,
    @Valid Gerance gerance,
    @Valid long police,
    @Valid LocalDate date 
  ){
    service.addRequest(client,localite,gerance,police,date);
    return "list_requests";
  }
}
