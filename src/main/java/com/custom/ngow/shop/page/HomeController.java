package com.custom.ngow.shop.page;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController extends BaseController {

  @GetMapping
  public String home(Model model) {
    addHeaderDataToModel(model);
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

  @GetMapping("/all-products")
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

  @GetMapping("/search")
  public String search(@RequestParam("q") String query, HttpSession session, Model model) {
    List<String> history = (List<String>) session.getAttribute(SEARCH_HISTORY_KEY);
    if (history == null) {
      history = new ArrayList<>();
    }

    history.remove(query);
    history.addFirst(query);

    if (history.size() > 6) {
      history = new ArrayList<>(history.subList(0, 6));
    }
    session.setAttribute(SEARCH_HISTORY_KEY, history);

    return "view/pages/shop-5-columns";
  }

}
