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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.radeel.DuplicataManagement.model.Admin;
import com.radeel.DuplicataManagement.model.Client;
import com.radeel.DuplicataManagement.model.ClientCategory;
import com.radeel.DuplicataManagement.repository.AdminRepository;
import com.radeel.DuplicataManagement.repository.ClientCategoryRepository;
import com.radeel.DuplicataManagement.repository.ClientRepository;
import com.radeel.DuplicataManagement.service.UserManager;

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

  @Autowired
  private AdminRepository adminRepository;

  @Autowired
  private UserManager userManager;

  @Autowired
  private PasswordEncoder passwordEncoder;

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
    Admin admin = new Admin(
      0,
      "admin",
      "admin",
      passwordEncoder.encode("admin"),
      new ArrayList<>()
    );
    adminRepository.save(admin);
  }
}
