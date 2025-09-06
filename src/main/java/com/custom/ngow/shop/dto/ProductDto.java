package com.custom.ngow.shop.dto;

import java.math.BigDecimal;
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
  private Set<ProductColor> colors;
  private Set<ProductSize> sizes;
  private Set<ProductImageDto> images;
}
