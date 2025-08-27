package com.custom.ngow.shop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.custom.ngow.shop.common.MessageUtil;
import com.custom.ngow.shop.entity.ProductColor;
import com.custom.ngow.shop.exception.CustomException;
import com.custom.ngow.shop.repository.ProductColorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductColorService {

  private final ProductColorRepository productColorRepository;
  private final MessageUtil messageUtil;

  public List<ProductColor> getByProductId(Long productId) {
    return productColorRepository.findByProductId(productId);
  }

  public ProductColor getByColorId(Long colorId) {
    return productColorRepository
        .findById(colorId)
        .orElseThrow(
            () ->
                new CustomException(
                    messageUtil, "", new String[] {"Color id: " + colorId}, "error.notExist"));
  }

  public ProductColor save(ProductColor productColor) {
    return productColorRepository.save(productColor);
  }
}
