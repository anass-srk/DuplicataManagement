package com.radeel.DuplicataManagement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
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
import com.radeel.DuplicataManagement.model.DuplicataType;
import com.radeel.DuplicataManagement.model.ElectricityDuplicata;
import com.radeel.DuplicataManagement.model.Gerance;
import com.radeel.DuplicataManagement.model.Month;
import com.radeel.DuplicataManagement.model.RequestState;
import com.radeel.DuplicataManagement.model.RequestStatus;
import com.radeel.DuplicataManagement.repository.AdminRepository;
import com.radeel.DuplicataManagement.repository.ClientCategoryRepository;
import com.radeel.DuplicataManagement.repository.ClientRepository;
import com.radeel.DuplicataManagement.repository.DuplicataTypeRepository;
import com.radeel.DuplicataManagement.repository.ElectricityDuplicataRepository;
import com.radeel.DuplicataManagement.repository.MonthRepository;
import com.radeel.DuplicataManagement.repository.RequestStatusRepository;
import com.radeel.DuplicataManagement.service.DuplicataManager;
import com.radeel.DuplicataManagement.service.UserManager;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@SpringBootApplication
public class DuplicataManagementApplication implements CommandLineRunner{

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private AdminRepository adminRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private MonthRepository monthRepository;

  @Autowired
  private DuplicataTypeRepository duplicataTypeRepository;

  @Autowired
  private RequestStatusRepository requestStatusRepository;


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
    final List<Month> months = List.of(
      new Month((short)1,"JAN", new ArrayList<>(),new ArrayList<>(),new ArrayList<>()),
      new Month((short)2,"FEB", new ArrayList<>(),new ArrayList<>(),new ArrayList<>()),
      new Month((short)3,"MAR", new ArrayList<>(),new ArrayList<>(),new ArrayList<>()),
      new Month((short)4,"APR", new ArrayList<>(),new ArrayList<>(),new ArrayList<>()),
      new Month((short)5,"MAY", new ArrayList<>(),new ArrayList<>(),new ArrayList<>()),
      new Month((short)6,"JUN", new ArrayList<>(),new ArrayList<>(),new ArrayList<>()),
      new Month((short)7,"JUL", new ArrayList<>(),new ArrayList<>(),new ArrayList<>()),
      new Month((short)8,"AUG", new ArrayList<>(),new ArrayList<>(),new ArrayList<>()),
      new Month((short)9,"SEP", new ArrayList<>(),new ArrayList<>(),new ArrayList<>()),
      new Month((short)10,"OCT",new ArrayList<>(),new ArrayList<>(),new ArrayList<>()),
      new Month((short)11,"NOV",new ArrayList<>(),new ArrayList<>(),new ArrayList<>()),
      new Month((short)12,"DEC",new ArrayList<>(),new ArrayList<>(),new ArrayList<>())
    );
    monthRepository.saveAllAndFlush(months);

    final List<DuplicataType> types = List.of(
      new DuplicataType((short)1,Gerance.ELECTRICITY.name(),new ArrayList<>()),
      new DuplicataType((short)2,Gerance.WATER.name(),new ArrayList<>())
    );
    duplicataTypeRepository.saveAllAndFlush(types);

    final List<RequestStatus> statuses = List.of(
      new RequestStatus((short)0,RequestState.PENDING.name(),new ArrayList<>()),
      new RequestStatus((short)1,RequestState.APPROVED.name(),new ArrayList<>()),
      new RequestStatus((short)2,RequestState.REFUSED.name(),new ArrayList<>())
    );
    requestStatusRepository.saveAllAndFlush(statuses);
  }
}
