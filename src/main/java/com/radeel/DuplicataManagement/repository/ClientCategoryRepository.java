package com.radeel.DuplicataManagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radeel.DuplicataManagement.model.ClientCategory;

public interface ClientCategoryRepository extends JpaRepository<ClientCategory,Short> {
  boolean existsByName(String name);
  Optional<ClientCategory> findByName(String name);
}
