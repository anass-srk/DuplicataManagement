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
@Table(name = "DUPL_WAT")
public class WaterDuplicata {
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
  private String usage;

  @Column(name = "COD_BANK")
  private int bank_code;

  @Column(columnDefinition = "biguint NOT NULL")
  private long cons;

  @Column(columnDefinition = "biguint NOT NULL")
  private long index1;

  @Column(columnDefinition = "biguint NOT NULL")
  private long index2;

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
  private BigDecimal mntht1;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mntht2;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mntht3;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mntht4;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mntht5;

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

  @Column(columnDefinition = "biguint NOT NULL")
  private long constra1;

  @Column(columnDefinition = "biguint NOT NULL")
  private long constra2;

  @Column(columnDefinition = "biguint NOT NULL")
  private long constra3;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal prixa1;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal prixa2;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal prixa3;

   @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mnthta1;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mnthta2;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mnthta3;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mnttvaa1;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mnttvaa2;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mnttvaa3;

  private int guichet;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal timbre;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal tva;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal red_fix_eau;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal red_fix_ass;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal tva_rdf_eau;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal tva_rdf_ass;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal mnt_ht;

  @Column(columnDefinition = "unum NOT NULL")
  private BigDecimal net;

  private LocalDate date_paie;

  private LocalDate date_i1;

  private LocalDate date_i2;

  private int nbj;
}
