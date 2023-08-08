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
@Table(name = "REQ_STS")
public class RequestStatus {
  @Id
  @GeneratedValue
  @Column(name = "RES_ID")
  private short id;

  @Column(name = "RES_NAME",nullable = false,unique = true)
  private String name;

  @OneToMany(mappedBy = "status",cascade = CascadeType.REMOVE)
  private List<Request> requests;

}
