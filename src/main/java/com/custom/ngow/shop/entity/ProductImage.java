package com.custom.ngow.shop.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String imageUrl;

  private String altText;
  private Boolean isMain = false;
  private Integer sortOrder = 0;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "color_id")
  private ProductColor color;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_size_id")
  private ProductSize size;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @CreationTimestamp private LocalDateTime createdAt;
}
