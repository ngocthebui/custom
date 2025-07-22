package com.custom.ngow.shop.entity;

import com.custom.ngow.shop.constant.OrderStatus;
import com.custom.ngow.shop.constant.PaymentStatus;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String orderNumber;

  @Column(nullable = false)
  private BigDecimal subtotal;

  private BigDecimal shippingCost = BigDecimal.ZERO;
  private BigDecimal tax = BigDecimal.ZERO;
  private BigDecimal discount = BigDecimal.ZERO;

  @Column(nullable = false)
  private BigDecimal totalAmount;

  @Enumerated(EnumType.STRING)
  private OrderStatus status = OrderStatus.PENDING;

  @Enumerated(EnumType.STRING)
  private PaymentStatus paymentStatus = PaymentStatus.PENDING;

  private String notes;

//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "user_id", nullable = false)
//  private User user;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "shipping_address_id")
  private Address shippingAddress;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<OrderItem> orderItems = new ArrayList<>();

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Payment> payments = new ArrayList<>();

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;
}
