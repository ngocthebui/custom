package com.custom.ngow.shop.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.custom.ngow.shop.constant.ProductBadge;
import com.custom.ngow.shop.constant.ProductStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(
    exclude = {"colors", "sizes", "categories", "images", "reviews", "cartItems", "orderItems"})
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String sku; // Mã sản phẩm

  @Column(nullable = false)
  private BigDecimal price;

  @Column(nullable = false)
  private Integer salePercentage;

  @Column(nullable = false)
  private BigDecimal salePrice;

  private Integer stockQuantity;

  @Column(length = 1000, columnDefinition = "TEXT")
  private String description;

  private String material; // Chất liệu
  private Integer strapQuantity; // Số lượng dây đeo
  private String innerPocket; // Ngăn trong
  private String handleLength; // Chiều dài tay cầm
  private Boolean removableStrap; // Dây đeo có thể tháo rời
  private Boolean adjustableStrap; // Dây đeo có thể điều chỉnh
  private String lockType; // Loại khóa cài
  private String strapLength; // Chiều dài dây đeo
  private String strapTotalLength; // Tổng chiều dài dây
  private String weight; // Trọng lượng
  private String width; // Chiều rộng
  private String depth; // Chiều sâu
  private String height; // Chiều cao

  @Enumerated(EnumType.STRING)
  private ProductStatus status = ProductStatus.INACTIVE;

  @Enumerated(EnumType.STRING)
  private ProductBadge badge;

  private Integer countdownTimer = 0; // second
  private Boolean isTopSale = false;
  private Boolean isFeatured = false;
  private Integer viewCount = 0;
  private Double rating = 0.0;
  private Integer reviewCount = 0;

  @ManyToMany
  @JoinTable(
      name = "category_product",
      joinColumns = @JoinColumn(name = "product_id"),
      inverseJoinColumns = @JoinColumn(name = "category_id"))
  private Set<Category> categories = new HashSet<>();

  @OneToMany(
      mappedBy = "product",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  private Set<ProductColor> colors = new HashSet<>();

  @OneToMany(
      mappedBy = "product",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  private Set<ProductSize> sizes = new HashSet<>();

  @OneToMany(
      mappedBy = "product",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  private Set<ProductImage> images = new HashSet<>();

  @OneToMany(
      mappedBy = "product",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  private Set<Review> reviews = new HashSet<>();

  @OneToMany(
      mappedBy = "product",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  private Set<CartItem> cartItems = new HashSet<>();

  @OneToMany(
      mappedBy = "product",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  private Set<OrderItem> orderItems = new HashSet<>();

  @CreationTimestamp private LocalDateTime createdAt;

  @UpdateTimestamp private LocalDateTime updatedAt;
}
