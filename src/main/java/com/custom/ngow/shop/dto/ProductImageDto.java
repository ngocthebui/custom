package com.custom.ngow.shop.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDto {
  private Long id;
  private String imageUrl;
  private String altText;
  private Boolean isMain = false;
  private Integer sortOrder = 0;
  private Long colorId;
  private Long sizeId;
  private MultipartFile image;

  public ProductImageDto(
      Long id,
      String imageUrl,
      String altText,
      Boolean isMain,
      Integer sortOrder,
      Long colorId,
      Long sizeId) {
    this.id = id;
    this.imageUrl = imageUrl;
    this.altText = altText;
    this.isMain = isMain;
    this.sortOrder = sortOrder;
    this.colorId = colorId;
    this.sizeId = sizeId;
  }
}
