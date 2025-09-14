package com.custom.ngow.shop.entity.composite;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SearchHistoryId implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  private String userEmail;
  private String searchQuery;
}
