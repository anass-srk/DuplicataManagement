package com.radeel.DuplicataManagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radeel.DuplicataManagement.model.Location;
import com.radeel.DuplicataManagement.model.Place;

public interface PlaceRepository extends JpaRepository<Place,Long> {
  boolean existsByLocationAndPoliceElectricity(Location location,long policeElectricity);
  Optional<Place> findByLocationAndPoliceElectricity(Location location,long policeElectricity);
}
