package com.custom.ngow.shop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.custom.ngow.shop.dto.CartItemRequest;
import com.custom.ngow.shop.service.CartCompositeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

  private final CartCompositeService cartCompositeService;

  @PostMapping("/add")
  public ResponseEntity<Void> addToCart(@RequestBody CartItemRequest cart) {
    cartCompositeService.addItemToCart(cart);
    return ResponseEntity.ok().build();
  }
}
