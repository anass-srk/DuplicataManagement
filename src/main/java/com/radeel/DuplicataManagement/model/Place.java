package com.radeel.DuplicataManagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

  @Column(name = "SECT",nullable = false)
  private short sect;

  @Column(name = "TRN",nullable = false)
  private short trn;

  @Column(name = "ORD",nullable = false)
  private short ord;

  @Column(name = "ADRESSE",nullable = false)
  private String address;

  @ManyToOne
  @JoinColumn(name = "CLI_ID")
  private Client client;

  @Column(name = "N_COMPT_ELE",nullable = false)
  private long electricityMeter;

  @Column(name = "N_COMPT_EAU",nullable = false)
  private short waterMeter;

  @Column(name = "N_ROUE",nullable = false)
  private short wheelCount;
}
