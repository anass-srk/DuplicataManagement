package com.radeel.DuplicataManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.radeel.DuplicataManagement.model.Client;
import com.radeel.DuplicataManagement.model.Verification;

public interface VerificationRepository extends JpaRepository<Verification,String> {
  long countByClient(Client client);
  List<Verification> findByClient(Client client);
  @Query(value = "SELECT VERF_UUID,CLI_ID,VERF_DATE FROM VERF WHERE CLI_ID = ?1 ORDER BY VERF_DATE DESC",nativeQuery = true)
  Verification findLastByClient(long id);
  long deleteByClient(Client client);
}
