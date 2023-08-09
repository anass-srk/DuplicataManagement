package com.radeel.DuplicataManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radeel.DuplicataManagement.model.ElectricityDuplicata;

public interface ElectricityDuplicataRepository extends JpaRepository<ElectricityDuplicata,Long> {
  
}
