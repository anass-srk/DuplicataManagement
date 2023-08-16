package com.radeel.DuplicataManagement.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.radeel.DuplicataManagement.model.Admin;
import com.radeel.DuplicataManagement.model.Client;
import com.radeel.DuplicataManagement.model.ClientCategory;
import com.radeel.DuplicataManagement.model.Verification;
import com.radeel.DuplicataManagement.repository.AdminRepository;
import com.radeel.DuplicataManagement.repository.ClientCategoryRepository;
import com.radeel.DuplicataManagement.repository.ClientRepository;
import com.radeel.DuplicataManagement.repository.VerificationRepository;
import com.radeel.DuplicataManagement.util.UserResponse;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;


@Transactional
@Service
public class UserManager implements UserService,UserDetailsService{

  @Value("${verification.max_links}")
  private long max_links;

  @Value("${verification.max_duration}")
  private long max_duration;

  @Value("${verification.cooldown}")
  private long cooldown;

  @Autowired
  private ClientCategoryRepository clientCategoryRepository;
  
  @Autowired
  private ClientRepository clientRepository;

  @Autowired
  private AdminRepository adminRepository;

  @Autowired
  private EmailService emailService;

  @Autowired
  private VerificationRepository verificationRepository;

  @Override
  public List<UserResponse> getAllUsers() {
    List<UserResponse> users = clientRepository.findAll().stream().map(UserResponse::fromClient).collect(Collectors.toList());
    adminRepository.findAll().stream().map(UserResponse::fromAdmin).forEach(users::add);
    return users;
  }

  @Override
  public List<ClientCategory> getAllClientCategories() {
    return clientCategoryRepository.findAll();
  }

  @Override
  public ClientCategory addClientCategory(String name) {
    var cat = clientCategoryRepository.findByName(name);
    if(cat.isPresent()){
      return cat.get();
    }
    return clientCategoryRepository.save(new ClientCategory((short)0,name,new ArrayList<>()));
  }

  @Override
  public boolean clientCategoryExists(String name) {
    return clientCategoryRepository.existsByName(name);
  }

  @Override
  public void removeClientCategory(String name) {
    clientCategoryRepository.findByName(name).ifPresentOrElse(
      cat -> clientCategoryRepository.delete(cat),
      () -> {
        throw new IllegalStateException(String.format(
        "No category by the name %s exists !", name));
      }
    );
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var user = adminRepository.findByEmail(username);
    if(user.isPresent()){
      return user.get();
    }else{
      return clientRepository.findByEmail(username).orElseThrow(
        () -> new UsernameNotFoundException(username)
      );
    }
  }

  @Override
  public boolean adminExists(String email) {
    return adminRepository.existsByEmail(email);
  }

  @Override
  public boolean emailIsUsed(String email) {
    return adminRepository.existsByEmail(email) || clientRepository.existsByEmail(email);
  }

  @Override
  public ClientCategory getClientCategory(String name) {
    return clientCategoryRepository.findByName(name).get();
  }

  @Override
  public void addClient(Client client) {
    if(emailIsUsed(client.getEmail())){
      throw new IllegalStateException(String.format(
        "The email %s is already in use !",client.getEmail()
      ));
    }
    var cat = client.getCategory();
    cat.getClients().add(client);
    clientCategoryRepository.save(cat);
    clientRepository.save(client);
  }
  
  @Override
  public void addAdmin(Admin admin){
    if(emailIsUsed(admin.getEmail())){
      throw new IllegalStateException(String.format(
        "The email %s is already in use !",admin.getEmail()
      ));
    }
    adminRepository.save(admin);
  }

  public String saveVerification(Client client){
    String uuid;
    do{
      uuid = UUID.randomUUID().toString();
    }while(verificationRepository.existsById(uuid));
    var verf = new Verification(uuid,client,Instant.now());
    client.getVerifications().add(verf);
    clientRepository.save(client); 
    verificationRepository.save(verf);
    return uuid;
  }

  @Override
  public boolean addVerficationLink(Client client) throws MessagingException {
    if(verificationRepository.countByClient(client) >= max_links){
      var last = verificationRepository.findLastByClient(client.getId());
      if(last.getDate().plusSeconds(cooldown).isAfter(Instant.now())){
        return false;
      }
      client.setVerifications(new ArrayList<>());
      verificationRepository.deleteByClient(client);
    }
    String link = saveVerification(client);
    emailService.sendConfirmationEmail("/confirm_client?link=" + link,client.getEmail());
    return true;
  }

  @Override
  public boolean verifyClient(String link) {
    var verf = verificationRepository.findById(link);
    if(verf.isPresent() && verf.get().getDate().plusSeconds(max_duration).isAfter(Instant.now())){
      var client = verf.get().getClient();
      client.setActive(true);
      client.setVerifications(new ArrayList<>());
      verificationRepository.deleteByClient(client);
      clientRepository.save(client);
      return true;
    }
    return false;
  }

  @Override
  public void saveClient(Client client) {
    if(clientRepository.existsById(client.getId())){
      clientRepository.save(client);
    }else{
      throw new UsernameNotFoundException(
        String.format("No client with the email '%s' exists !",client.getEmail()));
    }
  }

  @Override
  public Client getClientById(long id) {
    return clientRepository.findById(id).orElseThrow(
      () -> new UsernameNotFoundException(
        String.format("No client with the id '%d' exists !",id))
    );
  }

  @Override
  public ClientCategory saveClientCategory(ClientCategory clientCategory) {
    return clientCategoryRepository.save(clientCategory);
  }

  @Override
  public Admin getAdminById(long id) {
    return adminRepository.findById(id).orElseThrow(
      () -> new UsernameNotFoundException(
        String.format("No admin with the id '%d' exists !",id))
    );
  }

  @Override
  public void saveAdmin(Admin admin) {
    if(adminRepository.existsById(admin.getId())){
      adminRepository.save(admin);
    }else{
      throw new UsernameNotFoundException(
        String.format("No admin with the email '%s' exists !",admin.getEmail()));
    }
  }

  @Override
  public void deleteClient(long id) {
    if(clientRepository.existsById(id)){
      clientRepository.deleteById(id);
    }else{
      throw new UsernameNotFoundException(
        String.format("No client with id '%d' exists !",id));
    }
  }

  @Override
  public void deleteAdmin(long id) {
    if(adminRepository.existsById(id)){
      adminRepository.deleteById(id);
    }else{
      throw new UsernameNotFoundException(
        String.format("No admin with the email '%d' exists !",id));
    }
  }
}
