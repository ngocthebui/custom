package com.custom.ngow.shop.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  private Long id;
  private String sessionId;
  private String note;
  private List<CartItemDto> items = new ArrayList<>();
}
