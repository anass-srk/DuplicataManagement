package com.radeel.DuplicataManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radeel.DuplicataManagement.model.Location;

public interface LocationRepository extends JpaRepository<Location,Short> {
  
}
