package com.custom.ngow.shop.dto;

import java.time.LocalDateTime;

import com.custom.ngow.shop.constant.UserRole;

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

  @NotBlank(message = "Email không được để trống")
  @Email(message = "Email không hợp lệ")
  private String email;

  @NotBlank(message = "Firstname không được để trống")
  private String firstName;

  @NotBlank(message = "Lastname không được để trống")
  private String lastName;

  private String imageUrl;
  private UserRole role;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
