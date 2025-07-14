package com.custom.ngow.shop.page;

import com.custom.ngow.shop.demoEntity.Collection;
import java.util.Arrays;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

public class BaseController {

  public void addHeaderDataToModel(Model model) {
    // collection
    List<Collection> collections = Arrays.asList(
        new Collection("Home Fashion 1", "/", "images/demo/home-fashion-1.jpg", false),
        new Collection("Home Fashion 2", "/", "images/demo/home-fashion-2.jpg", false),
        new Collection("Home Fashion 3", "/", "images/demo/home-fashion-3.jpg", false),
        new Collection("Home Fashion 4", "/", "images/demo/home-fashion-4.jpg", false),
        new Collection("Home Cosmetic", "/", "images/demo/home-cosmetic.jpg", false),
        new Collection("Home Skincare", "/", "images/demo/home-skin-care.jpg", false),
        new Collection("Home Decor", "/", "images/demo/home-decor.jpg", false),
        new Collection("Home Jewelry", "/", "images/demo/home-jewelry.jpg", false),
        new Collection("Home Electric Market", "/", "images/demo/home-electronic-market.jpg", false),
        new Collection("Home Pet Store", "/", "images/demo/home-pet-store.jpg", false),
        new Collection("Home Sneaker", "/", "images/demo/home-sneaker.jpg", false),
        new Collection("Home Book", "/", "images/demo/home-book.jpg", true)
    );

    model.addAttribute("collectionList", collections);

    // account
    if (isUserLoggedIn()) {
      model.addAttribute("userAccountLink", "/account");
      model.addAttribute("userLoginStatus", true);
    } else {
      model.addAttribute("userAccountLink", "/login");
      model.addAttribute("userLoginStatus", false);
    }
  }

  private boolean isUserLoggedIn() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null && authentication.isAuthenticated()
        && !authentication.getName().equals("anonymousUser");
  }

}
