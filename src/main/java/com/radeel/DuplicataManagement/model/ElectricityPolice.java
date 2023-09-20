package com.radeel.DuplicataManagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
  name = "EPOL",
  uniqueConstraints = @UniqueConstraint(
    name = "uniqueLocaliteAndPolice",
    columnNames = {"police","PLACE_ID"}
  )
)
public class ElectricityPolice {
  
  @Id
  @GeneratedValue
  private long id;

  @Column(nullable = false)
  private long police;

  private long account;

  @ManyToOne
  @JoinColumn(name = "PLACE_ID",nullable = false,unique = true)
  private Place place;
}
