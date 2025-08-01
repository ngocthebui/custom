package com.custom.ngow.shop.constant;

import lombok.Getter;

@Getter
public enum ProductBadge {
    HOT("hot", "Hot"),
    FLASH_SALE("flash-sale", "Flash Sale"),
    TRENDING("trend", "Trending"),
    NEW("new", "New"),
    SALE("sale", "20% OFF"),
    LIMITED("limit", "Limited");

    private final String className;
    private final String value;

    ProductBadge(String className, String value) {
        this.className = className;
        this.value = value;
    }
}
