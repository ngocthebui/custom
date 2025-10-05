package com.custom.ngow.shop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.custom.ngow.shop.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

  Optional<Cart> findByUserId(Long userId);

  @Query(
      """
          SELECT c FROM Cart c
          LEFT JOIN FETCH c.items
          WHERE c.user.id IN :userId
          """)
  Optional<Cart> findByUserIdFetch(@Param("userId") Long userId);
}
