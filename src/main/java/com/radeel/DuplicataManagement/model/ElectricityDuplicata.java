package com.radeel.DuplicataManagement.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "DUPL_ELE")
public class ElectricityDuplicata {
  @Id
  @GeneratedValue
  @Column(name = "DUPL_ID")
  private long id;

  @ManyToOne
  @JoinColumn(name = "PLACE_ID")
  private Place place;

  @ManyToOne
  @JoinColumn(name = "MON_ID")
  private Month month;

  @Column(columnDefinition = "smalluint NOT NULL")
  private short year;

  @Column(name = "USAGE")
  private short usage;

  @Column(name = "COD_BANK")
  private short bank_code;

  @Column(columnDefinition = "biguint NOT NULL")  
  private long idx1;

  @Column(columnDefinition = "biguint NOT NULL")  
  private long idx2;

  @Column(columnDefinition = "biguint NOT NULL")  
  private long cons;

  @Column(columnDefinition = "biguint NOT NULL")  
  private long constr1;
  
  @Column(columnDefinition = "biguint NOT NULL")  
  private long constr2;

  @Column(columnDefinition = "biguint NOT NULL")  
  private long constr3;

  @Column(columnDefinition = "biguint NOT NULL")  
  private long constr4;

  @Column(columnDefinition = "biguint NOT NULL")  
  private long constr5;

  @Column(columnDefinition = "biguint NOT NULL")  
  private long constr6;
  
  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal prix1;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal prix2;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal prix3;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal prix4;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal prix5;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal prix6;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mnt1;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mnt2;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mnt3;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mnt4;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mnt5;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mnt6;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mnttva1;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mnttva2;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mnttva3;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mnttva4;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mnttva5;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mnttva6;

  private short guichet;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mnt_cons;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal timbre;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal tppan;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal tva;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal taxe_entretien;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal tva_entr;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal taxe_location;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal tva_loc;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal taxe;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal tva_tot_taxe;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mnt_ht;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal net_apayer;

  private short nbr_taxe;

  private LocalDate date_paie;

  private short bonus;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal tva_bonus;

  private LocalDate date_i1;

  private LocalDate date_i2;

  private short nbj;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal rdf;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal tva_rdf;
}
