package com.radeel.DuplicataManagement.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "LOC")
public class Location {
  @Id
  @Column(name = "LOC_ID")
  private short id;

  @Column(name = "LOC_AGN")
  private String agence;

  @OneToMany(mappedBy = "location",cascade = CascadeType.REMOVE)
  private List<Place> places;
}
