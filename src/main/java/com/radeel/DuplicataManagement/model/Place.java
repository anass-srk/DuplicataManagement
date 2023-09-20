package com.radeel.DuplicataManagement.model;

import java.util.List;
import java.util.Optional;

import org.hibernate.annotations.Check;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "PLACE")
public class Place {
  @Id
  @GeneratedValue
  @Column(name = "PLACE_ID")
  private long id;

  @ManyToOne
  @JoinColumn(name = "LOC_ID")
  private Locality location;

  @Column(nullable = false)
  private short sect;

  @Column(nullable = false)
  private short trn;

  @Column(nullable = false)
  private short ord;

  @OneToMany(mappedBy = "place",cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
  private List<ElectricityPolice> electricityPolice;

  @OneToMany(mappedBy = "place",cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
  private List<WaterPolice> waterPolice;

  @Column(name = "ADRESSE")
  private String address;

  @ManyToOne
  @JoinColumn(name = "CLI_ID")
  private Client client;
/*
  @Column(name = "N_COMPT_ELE",columnDefinition = "biguint NOT NULL")
  private long electricityMeter;

  @Column(name = "N_COMPT_EAU",columnDefinition = "biguint NOT NULL")
  private long waterMeter;
*/

  @OneToMany(mappedBy = "place",cascade = CascadeType.REMOVE)
  private List<ElectricityDuplicata> electricityDuplicatas; 

  @OneToMany(mappedBy = "place",cascade = CascadeType.REMOVE)
  private List<WaterDuplicata> waterDuplicatas; 
}
