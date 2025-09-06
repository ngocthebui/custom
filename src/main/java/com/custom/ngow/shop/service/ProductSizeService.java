package com.custom.ngow.shop.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.custom.ngow.shop.common.MessageUtil;
import com.custom.ngow.shop.entity.ProductSize;
import com.custom.ngow.shop.exception.CustomException;
import com.custom.ngow.shop.repository.ProductSizeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductSizeService {

  private final ProductSizeRepository productSizeRepository;
  private final MessageUtil messageUtil;

  public Set<ProductSize> getByProductId(Long productId) {
    return productSizeRepository.findByProductId(productId);
  }

  public ProductSize getBySizeId(Long sizeId) {
    return productSizeRepository
        .findById(sizeId)
        .orElseThrow(
            () ->
                new CustomException(
                    messageUtil, "", new String[] {"Size id: " + sizeId}, "error.notExist"));
  }

  public ProductSize save(ProductSize productSize) {
    return productSizeRepository.save(productSize);
  }
}
