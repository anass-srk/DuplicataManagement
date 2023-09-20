package com.radeel.DuplicataManagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.radeel.DuplicataManagement.model.WaterPolice;

public interface WaterPoliceRepository extends JpaRepository<WaterPolice,Long> {
  List<WaterPolice> findByPolice(long police);
}
