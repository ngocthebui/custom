package com.custom.ngow.shop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResetPasswordRequest {
  @NotBlank(message = "{error.required}")
  @Email(message = "{error.invalid}")
  private String email;
}
