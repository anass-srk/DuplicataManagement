package com.radeel.DuplicataManagement.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class Error implements ErrorController{
  
  @RequestMapping("/error")
  public String errorHandler(HttpServletRequest request,Model model){
    var status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    if(status != null){
      var code = Integer.valueOf(status.toString());
      if(code == HttpStatus.NOT_FOUND.value()){
        return "404";
      }
      if(code == HttpStatus.FORBIDDEN.value()){
        return "403";
      }
      model.addAttribute("error_code",String.format("Error code %d",code));
      return "error";
    }
    return "error";
  }
}
