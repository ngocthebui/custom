package com.custom.ngow.shop.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "banners")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Banner {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String subtitle;

  @Column(nullable = false)
  private String imageUrl;

  @Column(nullable = false)
  private String link;

  private Integer sortOrder;

  private Boolean isActive = true;

  private Boolean isAlwaysView = false;

  private LocalDateTime startDate;
  private LocalDateTime endDate;

  @CreationTimestamp private LocalDateTime createdAt;

  @UpdateTimestamp private LocalDateTime updatedAt;

  public Banner(
      Long id,
      String title,
      String subtitle,
      String imageUrl,
      String link,
      Boolean isActive,
      Integer sortOrder) {
    this.id = id;
    this.title = title;
    this.subtitle = subtitle;
    this.imageUrl = imageUrl;
    this.link = link;
    this.sortOrder = sortOrder;
    this.isActive = isActive;
  }
}
