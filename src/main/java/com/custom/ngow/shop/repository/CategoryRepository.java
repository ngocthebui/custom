package com.custom.ngow.shop.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.custom.ngow.shop.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  boolean existsByNameIgnoreCase(String name);

  @Query(
      """
    SELECT DISTINCT p.id FROM Category c
    JOIN c.products p
    WHERE c.code = :code
    AND p.status = 'ACTIVE'
    ORDER BY p.id DESC
    """)
  Page<Long> findAllProductIdsByCategoryCode(@Param("code") String code, Pageable pageable);

  Optional<Category> findByCode(String code);
}
