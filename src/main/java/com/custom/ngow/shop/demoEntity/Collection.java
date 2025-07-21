package com.custom.ngow.shop.demoEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Collection {
  private String name;
  private String url;
  private String imageUrl;
  private boolean isNew;
}
