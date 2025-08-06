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
public class UserDto {
  @NotBlank(message = "Firstname không được để trống")
  private String firstName;

  @NotBlank(message = "Lastname không được để trống")
  private String lastName;

  @NotBlank(message = "Email không được để trống")
  @Email(message = "Email không hợp lệ")
  private String email;

  private String currentPassword;

  @NotBlank(message = "Mật khẩu không được để trống")
  @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
  private String password;

  @NotBlank(message = "Xác nhận mật khẩu không được để trống")
  private String confirmPassword;

  public boolean isPasswordMatching() {
    return password != null && password.equals(confirmPassword);
  }
}
