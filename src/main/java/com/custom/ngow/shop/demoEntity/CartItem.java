package com.custom.ngow.shop.demoEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
  private TrendingProduct product;
  private int quantity;
  private String size;
  private String colorClass;
}
