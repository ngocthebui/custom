package com.custom.ngow.shop.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductColorDto {

  private Long id;
  private String name;
  private String code;
  private List<String> imageUrls = new ArrayList<>();
}
