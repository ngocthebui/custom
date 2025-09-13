package com.custom.ngow.shop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.custom.ngow.shop.constant.ProductStatus;
import com.custom.ngow.shop.entity.Product;

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

  @Query("""
      SELECT DISTINCT p.id FROM Product p
      WHERE p.status = 'ACTIVE'
      """)
  Page<Long> findAllActiveProductIdsPaging(Pageable pageable);

  @Query(
      """
      SELECT DISTINCT p FROM Product p
      LEFT JOIN FETCH p.categories
      LEFT JOIN FETCH p.sizes
      WHERE p.id IN :ids
      ORDER BY p.createdAt DESC
      """)
  List<Product> findActiveProductsWithCollections(@Param("ids") List<Long> ids);

  @Query(
      """
          SELECT DISTINCT p FROM Product p
          LEFT JOIN FETCH p.categories
          LEFT JOIN FETCH p.sizes
          WHERE p.status = 'ACTIVE'
          ORDER BY p.createdAt DESC
          """)
  List<Product> findAllActiveProducts();

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
