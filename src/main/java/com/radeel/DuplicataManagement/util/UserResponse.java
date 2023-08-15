package com.radeel.DuplicataManagement.util;

import com.radeel.DuplicataManagement.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
  Role role;
  long id;
  String username;
  String email;
  String category;
}
