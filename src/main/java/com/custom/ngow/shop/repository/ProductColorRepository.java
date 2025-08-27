package com.custom.ngow.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.custom.ngow.shop.entity.ProductColor;

@Repository
public interface ProductColorRepository extends JpaRepository<ProductColor, Long> {

  List<ProductColor> findByProductId(Long productId);
}
