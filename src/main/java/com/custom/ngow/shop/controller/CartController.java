package com.custom.ngow.shop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.custom.ngow.shop.dto.CartItemRequest;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/cart")
public class CartController {

  @PostMapping("/add")
  public ResponseEntity<?> addToCart(@RequestBody CartItemRequest cart) {
    log.info(cart.toString());
    return ResponseEntity.ok().build();
  }
}
