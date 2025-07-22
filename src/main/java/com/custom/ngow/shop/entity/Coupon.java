package com.custom.ngow.shop.entity;

import com.custom.ngow.shop.constant.DiscountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "coupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String code;

  @Column(nullable = false)
  private String name;

  private String description;

  @Enumerated(EnumType.STRING)
  private DiscountType discountType;

  @Column(nullable = false)
  private BigDecimal discountValue;

  private BigDecimal minimumOrderAmount;
  private BigDecimal maximumDiscountAmount;
  private Integer usageLimit;
  private Integer usedCount = 0;

  @Column(nullable = false)
  private LocalDateTime validFrom;

  @Column(nullable = false)
  private LocalDateTime validTo;

  private Boolean isActive = true;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;
}
