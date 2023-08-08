package com.radeel.DuplicataManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radeel.DuplicataManagement.model.Place;

public interface PlaceRepository extends JpaRepository<Place,Long> {
  
}
