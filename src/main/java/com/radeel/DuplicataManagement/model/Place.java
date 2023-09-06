package com.radeel.DuplicataManagement.model;

import java.util.List;

import org.hibernate.annotations.Check;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
  private Location location;

  @Column(nullable = false)
  private short sect;

  @Column(nullable = false)
  private short trn;

  @Column(nullable = false)
  private short ord;

  @Column(name = "POL_ELE")
  private long policeElectricity;

  @Column(name = "POL_EAU")
  private long policeWater;

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
  @Column(name = "N_ROUE",nullable = false)
  @Check(constraints = "N_ROUE >= 0")
  private int wheelCount;

  @Column(name = "N_CONTR_ELE",nullable = false)
  private long electricityAccount;

  @Column(name = "N_CONTR_EAU",nullable = false)
  private long waterAccount;

  @OneToMany(mappedBy = "place",cascade = CascadeType.REMOVE)
  private List<ElectricityDuplicata> electricityDuplicatas; 

  @OneToMany(mappedBy = "place",cascade = CascadeType.REMOVE)
  private List<WaterDuplicata> waterDuplicatas; 
}
