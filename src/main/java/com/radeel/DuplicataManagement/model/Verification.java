package com.radeel.DuplicataManagement.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "VERF")
public class Verification {
  @Id
  @Column(name = "VERF_UUID")
  private String uuid;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "CLI_ID")
  private Client client;

  @Column(name = "VERF_DATE",nullable = false)
  private Instant date;
}
