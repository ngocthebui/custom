package com.custom.ngow.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.custom.ngow.shop.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {}
