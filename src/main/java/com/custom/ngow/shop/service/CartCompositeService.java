package com.custom.ngow.shop.service;

import org.springframework.stereotype.Service;

import com.custom.ngow.shop.dto.CartDto;
import com.custom.ngow.shop.service.impl.ClientCartServiceImpl;
import com.custom.ngow.shop.service.impl.UserCartServiceImpl;

import lombok.RequiredArgsConstructor;

@Service("cartItemCompositeService")
@RequiredArgsConstructor
public class CartCompositeService implements CartService {

  private final UserAuthenticationService userAuthenticationService;
  private final UserCartServiceImpl userCartServiceImpl;
  private final ClientCartServiceImpl clientCartServiceImpl;

  @Override
  public CartDto getOrCreateCart() {
    return getCurrentService().getOrCreateCart();
  }

  private CartService getCurrentService() {
    if (userAuthenticationService.isUserLoggedIn()) {
      return userCartServiceImpl;
    } else {
      return clientCartServiceImpl;
    }
  }
}
