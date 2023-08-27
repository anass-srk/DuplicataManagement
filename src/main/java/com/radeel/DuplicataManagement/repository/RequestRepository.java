package com.radeel.DuplicataManagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radeel.DuplicataManagement.model.Client;
import com.radeel.DuplicataManagement.model.DuplicataType;
import com.radeel.DuplicataManagement.model.Month;
import com.radeel.DuplicataManagement.model.Request;

public interface RequestRepository extends JpaRepository<Request,Long> {
  boolean existsByLocaliteAndPoliceAndTypeAndClientAndMonthAndYear(
    short localite,
    long police,
    DuplicataType type,
    Client client,
    Month month,
    short year
  );

  List<Request> findByClient(Client client);
}
