package com.custom.ngow.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {

  private Long productId;
  private Long sizeId;
  private Long colorId;
  private Integer quantity;
}
