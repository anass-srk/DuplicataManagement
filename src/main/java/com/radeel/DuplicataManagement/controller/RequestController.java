package com.radeel.DuplicataManagement.controller;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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

  @GetMapping("/list_requests")
  public String listRequests(Model model){
    var requests = service.getAllRequests();
    model.addAttribute("requests", requests);
    return "list_requests";
  }

  @PostMapping("/answer_request")
  public String answerRequest(
    @Valid long id,
    @Valid RequestState state,
    RedirectAttributes model
  ){
    if(!service.answerRequest(id, state)){
      model.addAttribute("error", true);
    }
    return "redirect:/list_requests";
  }

  @GetMapping("/client_requests")
  public String listClientRequests(
    Model model,
    @AuthenticationPrincipal Client client
    ){
    var requests = service.getClientRequests(client);
    model.addAttribute("requests", requests);
    return "client_requests";
  }

  @GetMapping("/create_request")
  public String createRequest(){
    return "create_request";
  }

  @PostMapping("/create_request")
  public String addRequest(
    @AuthenticationPrincipal Client client,
    @Valid short localite,
    @Valid Gerance gerance,
    @Valid long police,
    @Valid LocalDate date 
  ){
    service.addRequest(client,localite,gerance,police,date);
    return "redirect:/client_requests";
  }
}
