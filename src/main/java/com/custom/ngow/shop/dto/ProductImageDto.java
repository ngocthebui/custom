package com.custom.ngow.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDto {
  private String imageUrl;
  private String altText;
  private Boolean isMain;
  private String color;
  private String size;
}
