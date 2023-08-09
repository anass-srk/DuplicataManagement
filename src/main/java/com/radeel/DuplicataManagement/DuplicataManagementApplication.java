package com.radeel.DuplicataManagement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.radeel.DuplicataManagement.model.Client;
import com.radeel.DuplicataManagement.model.ClientCategory;
import com.radeel.DuplicataManagement.repository.ClientCategoryRepository;
import com.radeel.DuplicataManagement.repository.ClientRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@SpringBootApplication
public class DuplicataManagementApplication implements CommandLineRunner{

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private ClientRepository clientRepository;

  @Autowired
  private ClientCategoryRepository clientCategoryRepository;

	public static void main(String[] args) {
		SpringApplication.run(DuplicataManagementApplication.class, args);
	}

  @PostConstruct
  public void createDomains() throws DataAccessException, IOException{
    jdbcTemplate.execute(new String(Files.readAllBytes(Paths.get("src/main/resources/static/sql/domains.sql"))));
  }

  @Override
  @Transactional
  public void run(String... args) throws Exception {
    ClientCategory category = ClientCategory.builder()
    .Clients(new ArrayList<>())
    .name("A")
    .build();
    Client client = Client.builder()
    .email("test@test.com")
    .username("Me")
    .password("pwd")
    .category(category)
    .build();
    category.getClients().add(client);
    clientCategoryRepository.save(category);
    clientRepository.save(client);
  }

}
