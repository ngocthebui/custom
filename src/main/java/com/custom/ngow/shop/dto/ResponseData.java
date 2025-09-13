package com.custom.ngow.shop.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData<T> {

  private List<T> data = new ArrayList<>();
  private Integer page;
  private Integer totalPages;
}
