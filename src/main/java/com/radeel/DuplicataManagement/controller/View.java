package com.radeel.DuplicataManagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class View {

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

  @GetMapping("/create_client")
  public String createClient(){
    return "create_client";
  }

  @GetMapping("/verify_client")
  public String verifyClient(){
    return "verify_client";
  }

  @GetMapping("/list_users")
  public String listUsers(){
    return "list_users";
  }
}
