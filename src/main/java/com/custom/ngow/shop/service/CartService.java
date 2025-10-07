package com.custom.ngow.shop.service;

import com.custom.ngow.shop.dto.CartDto;
import com.custom.ngow.shop.dto.CartItemRequest;

public interface CartService {

  CartDto getOrCreateCart();

  void addItemToCart(CartItemRequest request);

  void removeItemFromCart(Long id);
}
