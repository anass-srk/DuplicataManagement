package com.radeel.DuplicataManagement.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.radeel.DuplicataManagement.model.Role;
import com.radeel.DuplicataManagement.util.UserResponse;

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
  public String listUsers(Model model){
    List<UserResponse> users = new ArrayList<>();
    users.add(new UserResponse(Role.CLIENT, 1,"Me", "test@test.com", "A"));
    users.add(new UserResponse(Role.CLIENT, 2,"Me2", "test@test2.com", "AB"));
    users.add(new UserResponse(Role.ADMIN, 1,"Admin", "admin@test.com", ""));
    model.addAttribute("users", users);
    return "list_users";
  }

  @GetMapping("/modify_client")
  public String modifyClient(){
    return "modify_client";
  }

  @GetMapping("/modify_admin")
  public String modifyAdmin(){
    return "modify_admin";
  }
}
