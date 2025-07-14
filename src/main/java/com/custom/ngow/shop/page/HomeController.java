package com.custom.ngow.shop.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

  @GetMapping
  public String home() {
    return "view/pages/index";
  }

  @GetMapping("/faq")
  public String faq() {
    return "view/pages/faq";
  }

  @GetMapping("/wish-list")
  public String wishList() {
    return "view/pages/wishlist";
  }

  @GetMapping("/login")
  public String login() {
    return "view/pages/login";
  }

  @GetMapping("/shop-5-columns")
  public String shop5columns() {
    return "view/pages/shop-5-columns";
  }

  @GetMapping("/shop-2-columns")
  public String shop2columns() {
    return "view/pages/shop-2-columns";
  }

  @GetMapping("/product-detail")
  public String productDetail() {
    return "view/pages/product-detail";
  }

}
