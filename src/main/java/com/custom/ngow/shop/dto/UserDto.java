package com.custom.ngow.shop.dto;

import java.time.LocalDateTime;

import com.custom.ngow.shop.constant.UserRole;
import com.custom.ngow.shop.constant.UserStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
  private Long id;

  @NotBlank(message = "{error.required}")
  @Email(message = "{error.invalid}")
  private String email;

  @NotBlank(message = "{error.required}")
  private String firstName;

  @NotBlank(message = "{error.required}")
  private String lastName;

  private String imageUrl;
  private UserRole role;
  private UserStatus status;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
