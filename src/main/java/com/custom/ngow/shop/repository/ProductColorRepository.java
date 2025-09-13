package com.custom.ngow.shop.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.custom.ngow.shop.entity.ProductColor;

@Repository
public interface ProductColorRepository extends JpaRepository<ProductColor, Long> {

  Set<ProductColor> findByProductId(Long productId);

  @Query(
      """
          SELECT DISTINCT pc FROM ProductColor pc
          LEFT JOIN FETCH pc.images
          LEFT JOIN FETCH pc.product
          WHERE pc.product.id IN :productIds
          """)
  Set<ProductColor> findByProductIds(@Param("productIds") Set<Long> productIds);
}
