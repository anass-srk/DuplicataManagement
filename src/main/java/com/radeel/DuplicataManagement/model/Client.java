package com.radeel.DuplicataManagement.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "CLI")
public class Client {
  @Id
  @GeneratedValue
  @Column(name = "CLI_ID")
  private long id;

  @Builder.Default
  @Column(name = "CLI_ACTIVE")
  private boolean active = false;

  @Column(name = "CLI_EMAIL",unique = true,nullable = false)
  private String email;

  @Column(name = "CLI_USERNAME",nullable = false)
  private String username;

  @Column(name = "CLI_PASSWORD",unique = true,nullable = false)
  private String passord;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "CAT_ID")
  private ClientCategory category;

  @OneToMany(mappedBy = "client",cascade = CascadeType.REMOVE)
  private List<Verification> verifications;

  @OneToMany(mappedBy = "client")
  private List<Place> places;
}
