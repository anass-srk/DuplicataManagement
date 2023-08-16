package com.radeel.DuplicataManagement.util;

import com.radeel.DuplicataManagement.model.Admin;
import com.radeel.DuplicataManagement.model.Client;
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
  public static UserResponse fromClient(Client client){
    return new UserResponse(
      Role.CLIENT,
      client.getId(),
      client.getUsername(),
      client.getEmail(),
      client.getCategory().getName()
      );
  }
  public static UserResponse fromAdmin(Admin admin){
    return new UserResponse(
      Role.ADMIN,
      admin.getId(),
      admin.getUsername(),
      admin.getEmail(),
      ""
    );
  }
}
