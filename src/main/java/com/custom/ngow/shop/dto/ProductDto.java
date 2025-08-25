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
  private int salePercent;

  private String material; // Chất liệu
  private int strapQuantity; // Số lượng dây đeo
  private String innerPocket; // Ngăn trong
  private String handleLength; // Chiều dài tay cầm
  private boolean removableStrap; // Dây đeo có thể tháo rời
  private boolean adjustableStrap; // Dây đeo có thể điều chỉnh
  private String lockType; // Loại khóa cài
  private String strapLength; // Chiều dài dây đeo
  private String strapTotalLength; // Tổng chiều dài dây
  private String weight; // Trọng lượng
  private String width; // Chiều rộng
  private String depth; // Chiều sâu
  private String height; // Chiều cao

  private List<ProductImageDto> images;
  private List<ProductSize> sizes;
  private ProductBadge badge;
  private List<ProductColor> colors;
  private Integer countdownTimer; // second
  private Boolean isTopSale;
  private Integer viewCount = 0;
  private Double rating = 0.0;
  private Integer reviewCount = 0;
  private Set<Category> categories;
}
