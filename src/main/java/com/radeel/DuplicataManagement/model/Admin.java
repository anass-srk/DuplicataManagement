package com.radeel.DuplicataManagement.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
public class Admin implements UserDetails {
  @Id
  @GeneratedValue
  @Column(name = "ADM_ID")
  private long id;

  @Column(name = "ADM_EMAIL",unique = true,nullable = false)
  private String email;

  @Column(name = "ADM_USERNAME",nullable = false)
  private String username;

  @Column(name = "ADM_PASSWORD",unique = true,nullable = false)
  private String password;

  @OneToMany(mappedBy = "admin",cascade = CascadeType.REMOVE)
  private List<Request> requests;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority(Role.ADMIN.name()));
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
