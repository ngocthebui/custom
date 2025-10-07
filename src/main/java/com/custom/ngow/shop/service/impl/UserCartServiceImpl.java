package com.custom.ngow.shop.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.custom.ngow.shop.common.MessageUtil;
import com.custom.ngow.shop.dto.CartDto;
import com.custom.ngow.shop.dto.CartItemDto;
import com.custom.ngow.shop.dto.CartItemRequest;
import com.custom.ngow.shop.entity.Cart;
import com.custom.ngow.shop.entity.CartItem;
import com.custom.ngow.shop.entity.Category;
import com.custom.ngow.shop.entity.Product;
import com.custom.ngow.shop.entity.ProductColor;
import com.custom.ngow.shop.entity.ProductImage;
import com.custom.ngow.shop.entity.ProductSize;
import com.custom.ngow.shop.entity.User;
import com.custom.ngow.shop.exception.CustomException;
import com.custom.ngow.shop.repository.CartItemRepository;
import com.custom.ngow.shop.repository.CartRepository;
import com.custom.ngow.shop.service.CartService;
import com.custom.ngow.shop.service.ProductColorService;
import com.custom.ngow.shop.service.ProductService;
import com.custom.ngow.shop.service.ProductSizeService;
import com.custom.ngow.shop.service.UserAuthenticationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCartServiceImpl implements CartService {

  private final CartRepository cartRepository;
  private final ModelMapper modelMapper;
  private final MessageUtil messageUtil;
  private final CartItemRepository cartItemRepository;
  private final UserAuthenticationService userAuthenticationService;
  private final ProductService productService;
  private final ProductSizeService productSizeService;
  private final ProductColorService productColorService;

  @Override
  public CartDto getOrCreateCart() {
    Cart cart = getOrCreateCartUser();

    List<CartItem> items = cart.getItems();
    CartDto cartDto = new CartDto();
    cartDto.setId(cart.getId());
    cartDto.setNote(cart.getNote());

    for (CartItem cartItem : items) {
      CartItemDto cartItemDto = new CartItemDto();
      cartItemDto.setId(cartItem.getId());
      cartItemDto.setQuantity(cartItem.getQuantity());
      cartItemDto.setCategoryName(cartItem.getCategoryName());
      cartItemDto.setCategoryCode(cartItem.getCategoryCode());
      cartItemDto.setProductName(cartItem.getProductName());
      cartItemDto.setSku(cartItem.getSku());
      cartItemDto.setSizeName(cartItem.getSizeName());
      cartItemDto.setColorCode(cartItem.getColorCode());
      cartItemDto.setImageUrl(cartItem.getImageUrl());

      Product product = cartItem.getProduct();
      cartItemDto.setPrice(product.getSalePrice());
      cartItemDto.setStockQuantity(product.getStockQuantity());

      cartDto.getItems().add(cartItemDto);
    }

    return cartDto;
  }

  private Cart getOrCreateCartUser() {
    User user = userAuthenticationService.getCurrentUser();
    Optional<Cart> cart = cartRepository.findByUserId(user.getId());

    if (cart.isPresent()) {
      return cart.get();
    } else {
      Cart newCart = new Cart();
      newCart.setUser(user);
      log.info("User {} created new cart", user.getEmail());
      return cartRepository.save(newCart);
    }
  }

  @Override
  public void addItemToCart(CartItemRequest request) {
    Cart cart = getOrCreateCartUser();

    Optional<CartItem> oldItem =
        cartItemRepository.findByCartAndProductAndSizeAndColor(
            cart.getId(), request.getProductId(), request.getSizeId(), request.getColorId());

    CartItem cartItem;
    if (oldItem.isPresent()) {
      cartItem = oldItem.get();
      cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
    } else {
      cartItem = new CartItem();
      cartItem.setQuantity(request.getQuantity());

      Product product = productService.getProductById(request.getProductId());
      Category category = product.getCategories().stream().findFirst().orElse(null);
      ProductSize productSize = productSizeService.getBySizeId(request.getSizeId());
      ProductColor productColor = productColorService.getByColorId(request.getColorId());

      cartItem.setCart(cart);
      cartItem.setProduct(product);
      cartItem.setSize(productSize);
      cartItem.setColor(productColor);

      if (category != null) {
        cartItem.setCategoryName(category.getName());
        cartItem.setCategoryCode(category.getCode());
      }

      cartItem.setProductName(product.getName());
      cartItem.setSku(product.getSku());
      cartItem.setSizeName(productSize.getName());
      cartItem.setColorCode(productColor.getCode());

      String imageUrl =
          productColor.getImages().stream()
              .map(ProductImage::getImageUrl)
              .filter(Objects::nonNull)
              .findFirst()
              .orElse(null);
      cartItem.setImageUrl(imageUrl);
    }
    cartItemRepository.save(cartItem);

    String currentUserEmail = userAuthenticationService.getCurrentUserEmail();
    log.info("User {} added new item to cart", currentUserEmail);
  }

  @Override
  public void removeItemFromCart(Long id) {
    CartItem cartItem =
        cartItemRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new CustomException(
                        messageUtil, "", new String[] {"CardItem id: " + id}, "error.notExist"));

    cartItemRepository.delete(cartItem);
  }
}
