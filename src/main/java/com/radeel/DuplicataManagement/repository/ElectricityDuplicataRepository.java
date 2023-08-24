package com.radeel.DuplicataManagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radeel.DuplicataManagement.model.ElectricityDuplicata;
import com.radeel.DuplicataManagement.model.Month;
import com.radeel.DuplicataManagement.model.Place;
import java.util.List;


public interface ElectricityDuplicataRepository extends JpaRepository<ElectricityDuplicata,Long> {
  Optional<ElectricityDuplicata> findByPlaceAndMonthAndYear(Place place,Month month,short year);
  boolean existsByPlaceAndMonthAndYear(Place place,Month month,short year);
  List<ElectricityDuplicata> findByPlace(Place place);
  boolean existsByPlace(Place place);
}
