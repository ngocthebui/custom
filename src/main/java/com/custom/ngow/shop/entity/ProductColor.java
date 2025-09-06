package com.custom.ngow.shop.entity;

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
import lombok.ToString;

@Entity
@Table(name = "product_colors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"product"})
public class ProductColor {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false) // #ff0000
  private String code;

  private String imageUrl;
  private Boolean isActive = true;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private Product product;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ProductColor c)) return false;
    return id != null && id.equals(c.id);
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : System.identityHashCode(this);
  }
}
