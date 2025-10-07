package com.custom.ngow.shop.service.impl;

import org.springframework.stereotype.Service;

import com.custom.ngow.shop.dto.CartDto;
import com.custom.ngow.shop.dto.CartItemRequest;
import com.custom.ngow.shop.service.CartService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientCartServiceImpl implements CartService {

  private static final String GUEST_CART_KEY = "userCart";

  private final HttpServletRequest request;

  @Override
  public CartDto getOrCreateCart() {
    HttpSession session = request.getSession(true);

    synchronized (session) {
      CartDto cart = (CartDto) session.getAttribute(GUEST_CART_KEY);

      if (cart == null) {
        cart = new CartDto();
        cart.setSessionId(session.getId());
        session.setAttribute(GUEST_CART_KEY, cart);
      }

      return cart;
    }
  }

  @Override
  public void addItemToCart(CartItemRequest request) {}
}
