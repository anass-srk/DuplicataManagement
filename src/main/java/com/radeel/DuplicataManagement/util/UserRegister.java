package com.radeel.DuplicataManagement.util;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegister{
  @NotBlank(message = " must not be blank !")
  private String username;

  @NotBlank(message = " must not be blank !")
  @Email(message = " must be valid email !")
  private String email;

  @Length(min = 8,message = " must be at least 8 characters long !")
  private String password;
}
