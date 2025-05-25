package com.custom.ngow.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Table(name = "products")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;
    String sku;
    String description;

    @Column(nullable = false)
    BigDecimal price;

    BigDecimal salePrice;

    @Column(nullable = false)
    Integer stock = 0;

    String material;
    String color;
    String dimensions;

    Boolean isFeatured = false;
    Boolean isNewArrival = false;
    Boolean isActive = true;
    Timestamp createdAt;
    Timestamp updatedAt;

}
