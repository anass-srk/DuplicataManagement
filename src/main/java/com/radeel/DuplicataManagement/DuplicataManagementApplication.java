package com.radeel.DuplicataManagement;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.radeel.DuplicataManagement.model.Client;
import com.radeel.DuplicataManagement.model.ClientCategory;
import com.radeel.DuplicataManagement.repository.ClientCategoryRepository;
import com.radeel.DuplicataManagement.repository.ClientRepository;

@SpringBootApplication
public class DuplicataManagementApplication implements CommandLineRunner{

  @Autowired
  private ClientRepository clientRepository;

  @Autowired
  private ClientCategoryRepository clientCategoryRepository;

	public static void main(String[] args) {
		SpringApplication.run(DuplicataManagementApplication.class, args);
	}

  @Override
  public void run(String... args) throws Exception {
    ClientCategory category = ClientCategory.builder()
    .Clients(new ArrayList<>())
    .name("A")
    .build();
    Client client = Client.builder()
    .email("test@test.com")
    .username("Me")
    .passord("pwd")
    .category(category)
    .build();
    category.getClients().add(client);
    clientCategoryRepository.save(category);
    clientRepository.save(client);
  }

}
