package com.radeel.DuplicataManagement.model;

import org.hibernate.annotations.Check;

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

  @ManyToOne
  @JoinColumn(name = "ADM_ID")
  private Admin admin;

  @ManyToOne
  @JoinColumn(name = "PLACE_ID")
  private Place place;

  @ManyToOne
  @JoinColumn(name = "MON_ID")
  private Month month;

  @Column(name = "YEAR",nullable = false)
  @Check(constraints = "YEAR >= 0")
  private short year;

  @ManyToOne
  @JoinColumn(name = "RES_ID")
  private RequestStatus status;

}
