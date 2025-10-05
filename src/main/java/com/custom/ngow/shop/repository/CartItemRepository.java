package com.custom.ngow.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
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

}
