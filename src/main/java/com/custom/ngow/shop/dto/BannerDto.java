package com.custom.ngow.shop.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerDto {

  private Long id;

  @NotBlank(message = "{error.required}")
  private String title;

  @NotBlank(message = "{error.required}")
  private String subtitle;

  private String imageUrl;

  @NotBlank(message = "{error.required}")
  private String link;

  private Integer sortOrder = 0;
  private Boolean isActive = true;
  private Boolean isAlwaysView = false;

  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private LocalDateTime startDate;

  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private LocalDateTime endDate;

  private MultipartFile imageFile;
}
