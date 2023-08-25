package com.radeel.DuplicataManagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radeel.DuplicataManagement.model.Month;
import com.radeel.DuplicataManagement.model.Place;
import com.radeel.DuplicataManagement.model.WaterDuplicata;

import java.util.List;


public interface WaterDuplicataRepository extends JpaRepository<WaterDuplicata,Long> {
  Optional<WaterDuplicata> findByPlaceAndMonthAndYear(Place place,Month month,short year);
  boolean existsByPlaceAndMonthAndYear(Place place,Month month,short year);
  List<WaterDuplicata> findByPlace(Place place);
  boolean existsByPlace(Place place);
}
