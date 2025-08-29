package com.custom.ngow.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.custom.ngow.shop.entity.ProductSize;

@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize, Long> {

  List<ProductSize> findByProductId(Long productId);
}
