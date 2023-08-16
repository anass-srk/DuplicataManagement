package com.radeel.DuplicataManagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radeel.DuplicataManagement.model.Client;

public interface ClientRepository extends JpaRepository<Client,Long>{
  boolean existsByEmail(String email);
  Optional<Client> findByEmail(String email);
}
