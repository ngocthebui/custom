package com.custom.ngow.shop.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.custom.ngow.shop.dto.CartDto;
import com.custom.ngow.shop.dto.CartItemDto;
import com.custom.ngow.shop.entity.Cart;
import com.custom.ngow.shop.entity.CartItem;
import com.custom.ngow.shop.entity.Category;
import com.custom.ngow.shop.entity.Product;
import com.custom.ngow.shop.entity.User;
import com.custom.ngow.shop.repository.CartItemRepository;
import com.custom.ngow.shop.repository.CartRepository;
import com.custom.ngow.shop.service.CartService;
import com.custom.ngow.shop.service.UserAuthenticationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCartServiceImpl implements CartService {

  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final UserAuthenticationService userAuthenticationService;
  private final ModelMapper modelMapper;

  @Override
  public CartDto getOrCreateCart() {
    User user = userAuthenticationService.getCurrentUser();

    Optional<Cart> cart = cartRepository.findByUserId(user.getId());

    if (cart.isPresent()) {
      List<CartItem> items = cart.get().getItems();
      CartDto cartDto = modelMapper.map(cart.get(), CartDto.class);

      for (CartItem cartItem : items) {
        CartItemDto cartItemDto = modelMapper.map(cartItem, CartItemDto.class);

        Product product = cartItem.getProduct();

        cartItemDto.setName(product.getName());
        cartItemDto.setPrice(product.getPrice());
        cartItemDto.setStockQuantity(product.getStockQuantity());

        Category category = product.getCategories().stream().findFirst().get();
        cartItemDto.setCategory(category.getName());
        cartItemDto.setCategoryCode(category.getCode());

        cartDto.getItems().add(cartItemDto);
      }

      return cartDto;
    } else {
      Cart newCart = new Cart();
      newCart.setUser(user);

      return modelMapper.map(cartRepository.save(newCart), CartDto.class);
    }
  }
}
