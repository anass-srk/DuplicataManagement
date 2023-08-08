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
@Table(name = "CLI_CAT")
public class ClientCategory {
  @Id
  @GeneratedValue
  @Column(name = "CAT_ID")
  private long id;

  @Column(name = "CAT_NAME",nullable = false,unique = true)
  private String name;

  @OneToMany(mappedBy = "category",cascade = CascadeType.REMOVE)
  private List<Client> Clients;
}
