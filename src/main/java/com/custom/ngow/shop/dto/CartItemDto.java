package com.custom.ngow.shop.dto;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  private Long id;
  private Integer quantity;
  private String categoryName;
  private String categoryCode;
  private String productName;
  private String sku;
  private String sizeName;
  private String colorCode;
  private String imageUrl;
  private BigDecimal price;
  private Integer stockQuantity;
}
