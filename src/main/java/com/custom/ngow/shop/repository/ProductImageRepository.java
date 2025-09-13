package com.custom.ngow.shop.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.custom.ngow.shop.dto.ProductImageDto;
import com.custom.ngow.shop.entity.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

  @Query(
      "SELECT new com.custom.ngow.shop.dto.ProductImageDto( "
          + "img.id, "
          + "img.imageUrl, "
          + "img.altText, "
          + "img.isMain, "
          + "img.sortOrder, "
          + "img.color.id, "
          + "img.color.name, "
          + "img.size.id, "
          + "img.size.name "
          + ") "
          + "FROM ProductImage img "
          + "WHERE img.product.id = :productId "
          + "ORDER BY img.sortOrder ASC ")
  Set<ProductImageDto> findByProductId(@Param("productId") Long productId);

  @Query(
      """
          SELECT DISTINCT pi FROM ProductImage pi
          LEFT JOIN FETCH pi.color
          LEFT JOIN FETCH pi.size
          LEFT JOIN FETCH pi.product
          WHERE pi.product.id IN :productIds
          ORDER BY pi.sortOrder ASC
          """)
  Set<ProductImage> findByProductIds(@Param("productIds") Set<Long> productIds);
}
