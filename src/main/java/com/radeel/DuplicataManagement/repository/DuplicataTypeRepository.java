package com.radeel.DuplicataManagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radeel.DuplicataManagement.model.DuplicataType;

public interface DuplicataTypeRepository extends JpaRepository<DuplicataType,Short>{
  Optional<DuplicataType> findByName(String name);
}
