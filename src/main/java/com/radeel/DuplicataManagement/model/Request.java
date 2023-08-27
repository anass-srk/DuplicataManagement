package com.radeel.DuplicataManagement.model;

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
@Table(name = "REQ")
public class Request {
  @Id
  @GeneratedValue
  @Column(name = "REQ_ID")
  private long id;

  @Column(name = "REQ_LOC")
  private short localite;

  @Column(name = "REQ_POL")
  private long police;

  @ManyToOne
  @JoinColumn(name = "ADM_ID")
  private Admin admin;

  @ManyToOne
  @JoinColumn(name = "CLI_ID")
  private Client client;

  @ManyToOne
  @JoinColumn(name = "MON_ID")
  private Month month;

  // @Column(name = "YEAR",nullable = false)
  // @Check(constraints = "YEAR >= 0")
  @Column(name = "YEAR",columnDefinition = "smalluint NOT NULL")
  private short year;

  @Column(name = "REQ_DATE",nullable = false)
  private LocalDate requestDate;

  @Column(name = "RES_DATE",nullable = true)
  private LocalDate responseDate;

  @ManyToOne
  @JoinColumn(name = "RES_ID")
  private RequestStatus status;

  @ManyToOne
  @JoinColumn(name = "REQ_TYPE")
  private DuplicataType type;
}
