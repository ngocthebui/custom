package com.custom.ngow.shop.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

  @NotBlank(message = "{error.required}")
  private String name;

  @NotNull(message = "{error.required}")
  private Long categoryId;

  @Size(max = 1000, message = "{error.maxlength}")
  private String description;

  @NotNull(message = "{error.required}")
  private BigDecimal price;

  @NotNull(message = "{error.required}")
  private BigDecimal salePrice;

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
}
