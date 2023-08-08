package com.radeel.DuplicataManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radeel.DuplicataManagement.model.Request;

public interface RequestRepository extends JpaRepository<Request,Long> {
  
}
