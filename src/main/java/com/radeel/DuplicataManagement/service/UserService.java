package com.radeel.DuplicataManagement.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
  public Optional<UserDetails> getUserById(long id);
  public Optional<UserDetails> getUserByEmail(String email);
  public boolean createUser(UserDetails user);
  public boolean modifyUser(long id);
}
