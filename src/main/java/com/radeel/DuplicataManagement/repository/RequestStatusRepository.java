package com.radeel.DuplicataManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radeel.DuplicataManagement.model.RequestStatus;

public interface RequestStatusRepository extends JpaRepository<RequestStatus,Short>{
  
}