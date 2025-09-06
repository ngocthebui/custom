package com.custom.ngow.shop.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageListDto {
  private Long productId;
  private Set<ProductImageDto> images = new HashSet<>();
}
