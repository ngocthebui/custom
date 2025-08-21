package com.custom.ngow.shop.service;

import org.springframework.stereotype.Service;

import com.custom.ngow.shop.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

  private final ProductRepository productRepository;
}
