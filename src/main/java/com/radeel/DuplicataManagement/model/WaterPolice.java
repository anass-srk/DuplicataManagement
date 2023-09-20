package com.radeel.DuplicataManagement.model;

import org.hibernate.annotations.Check;

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
  name = "WPOL",
  uniqueConstraints = @UniqueConstraint(
    name = "UniqueLocaliteAndPolice2",
    columnNames = {"police","PLACE_ID"}
  )
)
public class WaterPolice {
  
  @Id
  @GeneratedValue
  private long id;

  @Column(nullable = false)
  private long police;

  private long account;

  @Column(name = "N_ROUE",nullable = false)
  @Check(constraints = "N_ROUE >= 0")
  private int wheelCount;
  
  @ManyToOne
  @JoinColumn(name = "PLACE_ID",unique = true)
  private Place place;
}
