package com.custom.ngow.shop.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

  private Long id;

  @NotBlank(message = "{error.required}")
  private String name;

  @Size(max = 255, message = "{error.maxlength}")
  private String description;

  LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
