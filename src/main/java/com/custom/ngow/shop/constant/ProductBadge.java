package com.custom.ngow.shop.constant;

import lombok.Getter;

@Getter
public enum ProductBadge {
  NEW("new", "New"),
  HOT("hot", "Hot"),
  FLASH_SALE("flash-sale", "Flash Sale"),
  TRENDING("trend", "Trending"),
  SALE("sale", "SALE OFF"),
  LIMITED("limit", "Limited");

  private final String className;
  private final String value;

  ProductBadge(String className, String value) {
    this.className = className;
    this.value = value;
  }

  public static ProductBadge getByClassName(String className) {
    for (ProductBadge badge : ProductBadge.values()) {
      if (badge.className.equals(className)) {
        return badge;
      }
    }
    return NEW;
  }
}
