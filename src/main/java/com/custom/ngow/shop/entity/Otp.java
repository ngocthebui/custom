package com.custom.ngow.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "otps")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Otp {
  @Id private String email;

  @Column(nullable = false)
  private String otp;

  @Column(nullable = false)
  private long expiredTime;

  @Column(nullable = false)
  private boolean isActive;
}
