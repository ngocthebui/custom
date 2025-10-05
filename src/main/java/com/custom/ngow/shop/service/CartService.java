package com.custom.ngow.shop.service;

import com.custom.ngow.shop.dto.CartDto;

public interface CartService {

  CartDto getOrCreateCart();
}
