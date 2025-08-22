package com.custom.ngow.shop.dto;

import java.math.BigDecimal;
import java.util.List;

import com.custom.ngow.shop.entity.ProductColor;

import jakarta.validation.constraints.NotBlank;
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

  private String material; // Chất liệu
  private Integer strapQuantity; // Số lượng dây đeo
  private String innerPocket; // Ngăn trong
  private String handleLength; // Chiều dài tay cầm
  private Boolean removableStrap; // Dây đeo có thể tháo rời
  private Boolean adjustableStrap; // Dây đeo có thể điều chỉnh
  private String lockType; // Loại khóa cài
  private String strapLength; // Chiều dài dây đeo
  private String strapTotalLength; // Tổng chiều dài dây
  private String weight; // Trọng lượng
  private String width; // Chiều rộng
  private String depth; // Chiều sâu
  private String height; // Chiều cao

  private List<ProductColor> colors = List.of(new ProductColor());
}
