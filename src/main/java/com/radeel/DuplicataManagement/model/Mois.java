package com.radeel.DuplicataManagement.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
@Table(name = "MOIS")
public class Mois {
  @Id
  @GeneratedValue
  @Column(name = "MOIS_ID")
  private long id;

  @Column(name = "MOIS_NAME",nullable = false,unique = true)
  private String name;
}
