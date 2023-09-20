package com.radeel.DuplicataManagement.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.radeel.DuplicataManagement.model.ElectricityPolice;


public interface ElectricityPoliceRepository extends JpaRepository<ElectricityPolice,Long> {
  List<ElectricityPolice> findByPolice(long police);
}
