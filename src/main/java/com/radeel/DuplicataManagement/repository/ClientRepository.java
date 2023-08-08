package com.radeel.DuplicataManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radeel.DuplicataManagement.model.Client;

public interface ClientRepository extends JpaRepository<Client,Long>{
  
}
