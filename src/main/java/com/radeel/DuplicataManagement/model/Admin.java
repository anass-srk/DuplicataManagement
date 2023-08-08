package com.radeel.DuplicataManagement.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
@Table(name = "ADM")
public class Admin {
  @Id
  @GeneratedValue
  @Column(name = "ADM_ID")
  private long id;

  @Column(name = "ADM_EMAIL",unique = true,nullable = false)
  private String email;

  @Column(name = "ADM_USERNAME",nullable = false)
  private String username;

  @Column(name = "CLI_PASSWORD",unique = true,nullable = false)
  private String passord;

  @OneToMany(mappedBy = "admin",cascade = CascadeType.REMOVE)
  private List<Request> requests;

}
