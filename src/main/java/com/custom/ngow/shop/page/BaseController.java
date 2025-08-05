package com.custom.ngow.shop.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import com.custom.ngow.shop.demoEntity.CartItem;
import com.custom.ngow.shop.demoEntity.Collection;
import com.custom.ngow.shop.demoEntity.TrendingProduct;
import com.custom.ngow.shop.entity.Banner;

import jakarta.servlet.http.HttpSession;

public class BaseController {

  public static final String SEARCH_HISTORY_KEY = "searchHistory";

  @Autowired private HttpSession session;

  public void addHeaderDataToModel(Model model) {
    // collection
    List<Collection> collections =
        Arrays.asList(
            new Collection("Home Fashion 1", "/", "images/demo/home-fashion-1.jpg", false),
            new Collection("Home Fashion 2", "/", "images/demo/home-fashion-2.jpg", false),
            new Collection("Home Fashion 3", "/", "images/demo/home-fashion-3.jpg", false),
            new Collection("Home Fashion 4", "/", "images/demo/home-fashion-4.jpg", false),
            new Collection("Home Cosmetic", "/", "images/demo/home-cosmetic.jpg", false),
            new Collection("Home Skincare", "/", "images/demo/home-skin-care.jpg", false),
            new Collection("Home Decor", "/", "images/demo/home-decor.jpg", false),
            new Collection("Home Jewelry", "/", "images/demo/home-jewelry.jpg", false),
            new Collection(
                "Home Electric Market", "/", "images/demo/home-electronic-market.jpg", false),
            new Collection("Home Pet Store", "/", "images/demo/home-pet-store.jpg", false),
            new Collection("Home Sneaker", "/", "images/demo/home-sneaker.jpg", false),
            new Collection("Home Book", "/", "images/demo/home-book.jpg", true));

    model.addAttribute("collectionList", collections);

    // account
    if (isUserLoggedIn()) {
      model.addAttribute("userAccountLink", "/my-account");
      model.addAttribute("userLoginStatus", true);
    } else {
      model.addAttribute("userAccountLink", "/login");
      model.addAttribute("userLoginStatus", false);
    }

    // search history
    List<String> history = (List<String>) session.getAttribute(SEARCH_HISTORY_KEY);
    if (history == null) {
      history = Collections.emptyList();
    }
    model.addAttribute("searchHistory", history);

    // search trending product
    List<TrendingProduct> trendingProducts =
        Arrays.asList(
            new TrendingProduct(
                "Queen fashion long sleeve shirt, basic t-shirt",
                "images/products/product-1.jpg",
                "T-shirt",
                99.99,
                69.99),
            new TrendingProduct(
                "Champion Reverse Weave Pullover",
                "images/products/product-7.jpg",
                "Hoodie",
                149.99,
                109.99),
            new TrendingProduct(
                "Columbia PFG Fishing Shirt",
                "images/products/product-5.jpg",
                "Shorts",
                109.99,
                74.99),
            new TrendingProduct(
                "Puma Essentials Graphic Tee",
                "images/products/product-9.jpg",
                "Sweatshirt",
                69.99,
                49.99));
    List<List<TrendingProduct>> partitionedList = partition(trendingProducts, 2);

    model.addAttribute("partitionedList", partitionedList);

    // Shopping Cart: productSuggestion
    model.addAttribute("productSuggestion", trendingProducts);

    // Shopping Cart: cart items
    List<CartItem> cartItems = new ArrayList<>();
    for (TrendingProduct trendingProduct : trendingProducts) {
      CartItem cartItem = new CartItem();
      cartItem.setSize("L");
      cartItem.setQuantity(1);
      cartItem.setProduct(trendingProduct);
      cartItem.setColorClass("bg-sage-green");
      cartItems.add(cartItem);
    }
    model.addAttribute("cartItems", cartItems);

    // sliders
    List<Banner> bannerSlides =
        Arrays.asList(
            new Banner(
                1L,
                "Fall Winter Collection",
                "Vivamus lacinia odio vitae vestibulum vestibulum.",
                "images/slider/slider-1.jpg",
                "shop-default-list.html",
                true,
                0),
            new Banner(
                2L,
                "Spring Summer Collection",
                "Discover the elegance of renewal with soft tones and flowing textures.",
                "images/slider/slider-2.jpg",
                "shop-default-list.html",
                true,
                1),
            new Banner(
                3L,
                "Urban Edge Series",
                "Bold cuts and minimalist design for the modern city lifestyle.",
                "images/slider/slider-3.jpg",
                "shop-default-list.html",
                true,
                2));
    model.addAttribute("bannerSlides", bannerSlides);
  }

  public static <T> List<List<T>> partition(List<T> list, int size) {
    List<List<T>> partitions = new ArrayList<>();
    for (int i = 0; i < list.size(); i += size) {
      partitions.add(list.subList(i, Math.min(i + size, list.size())));
    }
    return partitions;
  }

  private boolean isUserLoggedIn() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null
        && authentication.isAuthenticated()
        && !authentication.getName().equals("anonymousUser");
  }
}
