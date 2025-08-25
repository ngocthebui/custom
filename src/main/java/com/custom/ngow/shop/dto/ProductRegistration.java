package com.custom.ngow.shop.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.custom.ngow.shop.constant.ProductBadge;
import com.custom.ngow.shop.entity.ProductColor;
import com.custom.ngow.shop.entity.ProductSize;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRegistration {

  private String mode;

  private Long id;

  @NotBlank(message = "{error.required}")
  private String name;

  @NotEmpty(message = "{error.required}")
  private List<Long> categoryIdList = new ArrayList<>();

  @NotNull(message = "{error.required}")
  private String badgeClassName;

  @Size(max = 1000, message = "{error.maxlength}")
  private String description;

  @NotNull(message = "{error.required}")
  private BigDecimal price;

  @NotNull(message = "{error.required}")
  private Integer salePercentage = 0;

  @NotNull(message = "{error.required}")
  private Integer stockQuantity;

  @NotEmpty(message = "{error.required}")
  private String material; // Chất liệu

  @NotNull(message = "{error.required}")
  private Integer strapQuantity; // Số lượng dây đeo

  @NotEmpty(message = "{error.required}")
  private String innerPocket; // Ngăn trong

  @NotEmpty(message = "{error.required}")
  private String handleLength; // Chiều dài tay cầm

  private Boolean removableStrap; // Dây đeo có thể tháo rời

  private Boolean adjustableStrap; // Dây đeo có thể điều chỉnh

  @NotEmpty(message = "{error.required}")
  private String lockType; // Loại khóa cài

  @NotEmpty(message = "{error.required}")
  private String strapLength; // Chiều dài dây đeo

  @NotEmpty(message = "{error.required}")
  private String strapTotalLength; // Tổng chiều dài dây

  @NotEmpty(message = "{error.required}")
  private String weight; // Trọng lượng

  @NotEmpty(message = "{error.required}")
  private String width; // Chiều rộng

  @NotEmpty(message = "{error.required}")
  private String depth; // Chiều sâu

  @NotEmpty(message = "{error.required}")
  private String height; // Chiều cao

  @NotEmpty(message = "{error.required}")
  private List<ProductColor> colors = new ArrayList<>();

  @NotEmpty(message = "{error.required}")
  private List<ProductSize> sizes = new ArrayList<>();

  public ProductRegistration(
      Long id,
      String name,
      ProductBadge badgeClassName,
      String description,
      BigDecimal price,
      Integer salePercentage,
      Integer stockQuantity,
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
      String height) {
    this.id = id;
    this.name = name;
    this.badgeClassName = badgeClassName.getClassName();
    this.description = description;
    this.price = price;
    this.salePercentage = salePercentage;
    this.stockQuantity = stockQuantity;
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
  }
}
