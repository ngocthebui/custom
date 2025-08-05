package com.custom.ngow.shop.demoEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrendingProduct {
  private String name;
  private String imageUrl;
  private String category;
  private double price;
  private double salePrice;
}
