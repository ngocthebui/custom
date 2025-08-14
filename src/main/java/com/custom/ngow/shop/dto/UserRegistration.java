package com.custom.ngow.shop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistration {
  @NotBlank(message = "{error.required}")
  private String firstName;

  @NotBlank(message = "{error.required}")
  private String lastName;

  @NotBlank(message = "{error.required}")
  @Email(message = "{error.invalid}")
  private String email;

  @NotBlank(message = "{error.required}")
  @Size(min = 8, message = "{error.minlength}")
  private String password;

  @NotBlank(message = "{error.required}")
  private String confirmPassword;

  public boolean isPasswordMatching() {
    return password != null && password.equals(confirmPassword);
  }
}
