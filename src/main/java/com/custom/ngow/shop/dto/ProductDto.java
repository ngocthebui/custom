package com.custom.ngow.shop.dto;

import java.util.List;

import com.custom.ngow.shop.constant.ProductBadge;
import com.custom.ngow.shop.entity.ProductColor;
import com.custom.ngow.shop.entity.ProductImage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
  private Long id;
  private String name;
  private double price;
  private double salePrice;
  private List<ProductImage> images;
  private List<String> sizes;
  private List<ProductBadge> badges;
  private List<ProductColor> colors;
  private Integer countdownTimer; // second
  private Boolean isTopSale;
}
