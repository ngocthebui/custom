package com.custom.ngow.shop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.custom.ngow.shop.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

  List<CartItem> findByCartId(Long cartId);

  //  @Query(
  //      """
  //          SELECT DISTINCT c FROM CartItem c
  //          LEFT JOIN FETCH c.images
  //          LEFT JOIN FETCH pc.product
  //          WHERE pc.product.id IN :productIds
  //          """)
  //  public List<CartItem> getCartItemsByCartId(@Param("cartId") Long cartId);

  @Query(
      "SELECT ci FROM CartItem ci "
          + "WHERE ci.cart.id = :cartId "
          + "AND ci.product.id = :productId "
          + "AND ci.size.id = :sizeId "
          + "AND ci.color.id = :colorId")
  Optional<CartItem> findByCartAndProductAndSizeAndColor(
      @Param("cartId") Long cartId,
      @Param("productId") Long productId,
      @Param("sizeId") Long sizeId,
      @Param("colorId") Long colorId);
}
