package com.radeel.DuplicataManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radeel.DuplicataManagement.model.Client;
import com.radeel.DuplicataManagement.model.Locality;
import com.radeel.DuplicataManagement.model.Place;

public interface PlaceRepository extends JpaRepository<Place,Long> {
  boolean existsByLocationAndSectAndTrnAndOrd(Locality location,short sect,short trn,short ord);
  Optional<Place> findByLocationAndSectAndTrnAndOrd(Locality location,short sect,short trn,short ord);
  List<Place> findByClient(Client client);
}
