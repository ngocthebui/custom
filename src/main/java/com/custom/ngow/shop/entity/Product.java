package com.custom.ngow.shop.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String sku; // Mã sản phẩm

  @Column(nullable = false)
  private BigDecimal price;

  private BigDecimal salePrice;
  private Integer stockQuantity;

  @Column(length = 1000)
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

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<ProductColor> colors;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<ProductSize> sizes;

  @Enumerated(EnumType.STRING)
  private ProductStatus status = ProductStatus.INACTIVE;

  private Boolean isFeatured = false;
  private Integer viewCount = 0;
  private Double rating = 0.0;
  private Integer reviewCount = 0;

  @ManyToMany
  @JoinTable(
      name = "category_product",
      joinColumns = @JoinColumn(name = "product_id"),
      inverseJoinColumns = @JoinColumn(name = "category_id"))
  private Set<Category> categories;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<ProductImage> images = new ArrayList<>();

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Review> reviews = new ArrayList<>();

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<CartItem> cartItems = new ArrayList<>();

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<OrderItem> orderItems = new ArrayList<>();

  @CreationTimestamp private LocalDateTime createdAt;

  @UpdateTimestamp private LocalDateTime updatedAt;
}
