package com.custom.ngow.shop.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.custom.ngow.shop.entity.Banner;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {

  @Query(
      """
        SELECT b
        FROM Banner b
        WHERE b.isActive = true
          AND (
               b.isAlwaysView = true
               OR (b.startDate <= :today AND b.endDate >= :today)
          )
        ORDER BY b.sortOrder ASC
      """)
  List<Banner> findAllSuitableBanner(@Param("today") LocalDateTime today);

  Long countByIsActive(Boolean isActive);
}
