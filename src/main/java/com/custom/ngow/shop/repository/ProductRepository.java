package com.custom.ngow.shop.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.custom.ngow.shop.constant.ProductStatus;
import com.custom.ngow.shop.entity.Category;
import com.custom.ngow.shop.entity.Product;
import com.custom.ngow.shop.entity.ProductColor;
import com.custom.ngow.shop.entity.ProductSize;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  @Query(
      """
        SELECT DISTINCT p FROM Product p
        LEFT JOIN FETCH p.categories
        LEFT JOIN FETCH p.colors
        LEFT JOIN FETCH p.sizes
        LEFT JOIN FETCH p.images
        WHERE p.id = :id
        """)
  Optional<Product> findByIdFetchAll(Long id);

  @Query("SELECT p.categories FROM Product p WHERE p.id = :productId")
  Set<Category> findCategoriesByProductId(@Param("productId") Long productId);

  @Query("SELECT p.colors FROM Product p WHERE p.id = :productId")
  Set<ProductColor> findColorsByProductId(@Param("productId") Long productId);

  @Query("SELECT p.sizes FROM Product p WHERE p.id = :productId")
  Set<ProductSize> findSizesByProductId(@Param("productId") Long productId);

  long countByStatus(ProductStatus status);

  Page<Product> findAllBySkuContainsIgnoreCase(String sku, Pageable pageable);

  @Query(
      """
        SELECT DISTINCT p FROM Product p
        LEFT JOIN FETCH p.categories
        LEFT JOIN FETCH p.colors
        LEFT JOIN FETCH p.sizes
        LEFT JOIN FETCH p.images
        WHERE p.sku = :sku AND p.status = 'ACTIVE'
        """)
  Optional<Product> findProductBySku(String sku);
}
