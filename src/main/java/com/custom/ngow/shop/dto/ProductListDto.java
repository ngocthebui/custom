package com.custom.ngow.shop.dto;

import java.math.BigDecimal;

import com.custom.ngow.shop.constant.ProductBadge;
import com.custom.ngow.shop.constant.ProductStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductListDto {
  private Long id;
  private String sku;
  private BigDecimal price;
  private Integer salePercentage;
  private ProductStatus status;
  private ProductBadge badge;
  private Double rating;
}
