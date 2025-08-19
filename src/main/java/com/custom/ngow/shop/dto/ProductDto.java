package com.custom.ngow.shop.dto;

import java.util.List;
import java.util.Set;

import com.custom.ngow.shop.constant.ProductBadge;
import com.custom.ngow.shop.entity.Category;
import com.custom.ngow.shop.entity.ProductColor;
import com.custom.ngow.shop.entity.ProductSize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
  private Long id;
  private String name;
  private String description;
  private String sku;
  private double price;
  private double salePrice;
  private double salePercent;
  private List<ProductImageDto> images;
  private List<ProductSize> sizes;
  private List<ProductBadge> badges;
  private List<ProductColor> colors;
  private Integer countdownTimer; // second
  private Boolean isTopSale;
  private Integer viewCount = 0;
  private Double rating = 0.0;
  private Integer reviewCount = 0;
  private Set<Category> categories;
}
