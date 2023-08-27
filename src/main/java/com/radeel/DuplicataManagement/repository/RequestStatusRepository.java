package com.radeel.DuplicataManagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radeel.DuplicataManagement.model.RequestStatus;


public interface RequestStatusRepository extends JpaRepository<RequestStatus,Short>{
  Optional<RequestStatus> findByName(String name);
}
