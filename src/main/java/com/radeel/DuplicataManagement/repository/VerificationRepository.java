package com.radeel.DuplicataManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radeel.DuplicataManagement.model.Verification;

public interface VerificationRepository extends JpaRepository<Verification,String> {
  
}
