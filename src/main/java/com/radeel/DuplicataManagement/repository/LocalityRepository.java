package com.radeel.DuplicataManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radeel.DuplicataManagement.model.Locality;

public interface LocalityRepository extends JpaRepository<Locality,Short> {
  
}
