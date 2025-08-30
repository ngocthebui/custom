package com.custom.ngow.shop.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.custom.ngow.shop.constant.ProductBadge;
import com.custom.ngow.shop.constant.ProductStatus;
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
  private String sku;
  private BigDecimal price;
  private Integer salePercentage;
  private BigDecimal salePrice;
  private Integer stockQuantity;
  private String description;

  private String material; // Chất liệu
  private Integer strapQuantity; // Số lượng dây đeo
  private String innerPocket; // Ngăn trong
  private String handleLength; // Chiều dài tay cầm
  private Boolean removableStrap; // Dây đeo có thể tháo rời
  private Boolean adjustableStrap; // Dây đeo có thể điều chỉnh
  private String lockType; // Loại khóa cài
  private String strapLength; // Chiều dài dây đeo
  private String strapTotalLength; // Tổng chiều dài dây
  private String weight; // Trọng lượng
  private String width; // Chiều rộng
  private String depth; // Chiều sâu
  private String height; // Chiều cao

  private ProductStatus status;
  private ProductBadge badge;

  private Integer countdownTimer; // second
  private Boolean isTopSale;
  private Boolean isFeatured;
  private Integer viewCount = 0;
  private Double rating = 0.0;
  private Integer reviewCount = 0;

  private Set<Category> categories;
  private List<ProductColor> colors;
  private List<ProductSize> sizes;
  private List<ProductImageDto> images;

  public ProductDto(
      Long id,
      String name,
      String sku,
      BigDecimal price,
      Integer salePercentage,
      BigDecimal salePrice,
      Integer stockQuantity,
      String description,
      String material,
      Integer strapQuantity,
      String innerPocket,
      String handleLength,
      Boolean removableStrap,
      Boolean adjustableStrap,
      String lockType,
      String strapLength,
      String strapTotalLength,
      String weight,
      String width,
      String depth,
      String height,
      ProductStatus status,
      ProductBadge badge,
      Integer countdownTimer,
      Boolean isTopSale,
      Boolean isFeatured,
      Integer viewCount,
      Double rating,
      Integer reviewCount) {
    this.id = id;
    this.name = name;
    this.sku = sku;
    this.price = price;
    this.salePercentage = salePercentage;
    this.salePrice = salePrice;
    this.stockQuantity = stockQuantity;
    this.description = description;
    this.material = material;
    this.strapQuantity = strapQuantity;
    this.innerPocket = innerPocket;
    this.handleLength = handleLength;
    this.removableStrap = removableStrap;
    this.adjustableStrap = adjustableStrap;
    this.lockType = lockType;
    this.strapLength = strapLength;
    this.strapTotalLength = strapTotalLength;
    this.weight = weight;
    this.width = width;
    this.depth = depth;
    this.height = height;
    this.status = status;
    this.badge = badge;
    this.countdownTimer = countdownTimer;
    this.isTopSale = isTopSale;
    this.isFeatured = isFeatured;
    this.viewCount = viewCount;
    this.rating = rating;
    this.reviewCount = reviewCount;
  }
}
