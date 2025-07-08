package com.custom.ngow.shop.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

  @GetMapping
  public String home() {
    return "view/pages/home";
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

}
