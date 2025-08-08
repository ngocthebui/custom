package com.custom.ngow.shop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.custom.ngow.shop.entity.Otp;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
  Optional<Otp> findByEmail(String email);
}
