package com.custom.ngow.shop.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.custom.ngow.shop.constant.ProductStatus;
import com.custom.ngow.shop.dto.ProductDto;
import com.custom.ngow.shop.dto.ProductRegistration;
import com.custom.ngow.shop.entity.Category;
import com.custom.ngow.shop.entity.Product;
import com.custom.ngow.shop.entity.ProductColor;
import com.custom.ngow.shop.entity.ProductSize;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  @Query(
      "SELECT new com.custom.ngow.shop.dto.ProductRegistration( "
          + "p.id, "
          + "p.name, "
          + "p.badge, "
          + "p.description, "
          + "p.price, "
          + "p.salePercentage, "
          + "p.stockQuantity, "
          + "p.material, "
          + "p.strapQuantity, "
          + "p.innerPocket, "
          + "p.handleLength, "
          + "p.removableStrap, "
          + "p.adjustableStrap, "
          + "p.lockType, "
          + "p.strapLength, "
          + "p.strapTotalLength, "
          + "p.weight, "
          + "p.width, "
          + "p.depth, "
          + "p.height "
          + ") "
          + "FROM Product p "
          + "WHERE p.id = :id")
  ProductRegistration findProductForUpdate(@Param("id") Long id);

  @Query("SELECT p.categories FROM Product p WHERE p.id = :productId")
  Set<Category> findCategoriesByProductId(@Param("productId") Long productId);

  @Query("SELECT p.colors FROM Product p WHERE p.id = :productId")
  List<ProductColor> findColorsByProductId(@Param("productId") Long productId);

  @Query("SELECT p.sizes FROM Product p WHERE p.id = :productId")
  List<ProductSize> findSizesByProductId(@Param("productId") Long productId);

  long countByStatus(ProductStatus status);

  Page<Product> findAllBySkuContainsIgnoreCase(String sku, Pageable pageable);

  @Query(
      "SELECT new com.custom.ngow.shop.dto.ProductDto( "
          + "p.id, "
          + "p.name, "
          + "p.sku, "
          + "p.price, "
          + "p.salePercentage, "
          + "p.salePrice, "
          + "p.stockQuantity, "
          + "p.description, "
          + "p.material, "
          + "p.strapQuantity, "
          + "p.innerPocket, "
          + "p.handleLength, "
          + "p.removableStrap, "
          + "p.adjustableStrap, "
          + "p.lockType, "
          + "p.strapLength, "
          + "p.strapTotalLength, "
          + "p.weight, "
          + "p.width, "
          + "p.depth, "
          + "p.height, "
          + "p.status, "
          + "p.badge, "
          + "p.countdownTimer, "
          + "p.isTopSale, "
          + "p.isFeatured, "
          + "p.viewCount, "
          + "p.rating, "
          + "p.reviewCount"
          + ") "
          + "FROM Product p "
          + "WHERE p.sku = :sku")
  ProductDto findProductDetailBySku(@Param("sku") String sku);
}
