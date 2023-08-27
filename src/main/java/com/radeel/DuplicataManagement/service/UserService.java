package com.radeel.DuplicataManagement.service;

import java.util.List;

import com.radeel.DuplicataManagement.model.Admin;
import com.radeel.DuplicataManagement.model.Client;
import com.radeel.DuplicataManagement.model.ClientCategory;
import com.radeel.DuplicataManagement.util.UserResponse;

import jakarta.mail.MessagingException;

public interface UserService {
  public ClientCategory getClientCategory(String name);
  public List<ClientCategory> getAllClientCategories();
  public ClientCategory addClientCategory(String name);
  public ClientCategory saveClientCategory(ClientCategory clientCategory);
  public boolean clientCategoryExists(String name);
  public void removeClientCategory(String name);
  public boolean adminExists(String email);
  public boolean emailIsUsed(String email);
  public List<UserResponse> getAllUsers();
  public void addClient(Client client);
  public Client getClientById(long id);
  public void saveClient(Client client);
  public boolean deleteClient(long id);
  public void addAdmin(Admin admin);
  public Admin getAdminById(long id);
  public void saveAdmin(Admin admin);  
  public boolean deleteAdmin(long id);
  public boolean addVerficationLink(Client client) throws MessagingException;
  public boolean verifyClient(String link);
}
