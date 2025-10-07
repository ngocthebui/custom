package com.custom.ngow.shop.page.shop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;

import com.custom.ngow.shop.common.MessageUtil;
import com.custom.ngow.shop.demoEntity.Collection;
import com.custom.ngow.shop.demoEntity.TrendingProduct;
import com.custom.ngow.shop.service.CartCompositeService;
import com.custom.ngow.shop.service.CategoryService;
import com.custom.ngow.shop.service.SearchHistoryService;

import jakarta.servlet.http.HttpSession;

public class BaseController {

  public static final String SEARCH_HISTORY_KEY = "searchHistory";

  @Autowired private HttpSession session;
  @Autowired private MessageUtil messageUtil;
  @Autowired private CategoryService categoryService;
  @Autowired private CartCompositeService cartService;

  @Qualifier("searchHistoryCompositeService")
  @Autowired
  private SearchHistoryService searchHistoryService;

  public void addDefaultToModel(Model model) {
    addTopbarDataToModel(model);
    addHeaderDataToModel(model);
  }

  public void addTopbarDataToModel(Model model) {
    List<String> topbarKeys =
        Arrays.asList("topbar.shopping.day", "topbar.free.shipping", "topbar.pickup.store");
    model.addAttribute("topbarKeys", topbarKeys);
  }

  public void addHeaderDataToModel(Model model) {
    addNavbarToModel(model);
    // collection
    List<Collection> collections =
        Arrays.asList(
            new Collection("Home Fashion 1", "/", "/images/demo/home-fashion-1.jpg", false),
            new Collection("Home Fashion 2", "/", "/images/demo/home-fashion-2.jpg", false),
            new Collection("Home Fashion 3", "/", "/images/demo/home-fashion-3.jpg", false),
            new Collection("Home Fashion 4", "/", "/images/demo/home-fashion-4.jpg", false),
            new Collection("Home Cosmetic", "/", "/images/demo/home-cosmetic.jpg", false),
            new Collection("Home Skincare", "/", "/images/demo/home-skin-care.jpg", false),
            new Collection("Home Decor", "/", "/images/demo/home-decor.jpg", false),
            new Collection("Home Jewelry", "/", "/images/demo/home-jewelry.jpg", false),
            new Collection(
                "Home Electric Market", "/", "/images/demo/home-electronic-market.jpg", false),
            new Collection("Home Pet Store", "/", "/images/demo/home-pet-store.jpg", false),
            new Collection("Home Sneaker", "/", "/images/demo/home-sneaker.jpg", false),
            new Collection("Home Book", "/", "/images/demo/home-book.jpg", true));

    model.addAttribute("collectionList", collections);

    // search history
    List<String> history = searchHistoryService.getHistory();
    model.addAttribute("searchHistory", history);

    // search trending product
    List<TrendingProduct> trendingProducts =
        Arrays.asList(
            new TrendingProduct(
                "Queen fashion long sleeve shirt, basic t-shirt",
                "/images/products/product-1.jpg",
                "T-shirt",
                99.99,
                69.99),
            new TrendingProduct(
                "Champion Reverse Weave Pullover",
                "/images/products/product-7.jpg",
                "Hoodie",
                149.99,
                109.99),
            new TrendingProduct(
                "Columbia PFG Fishing Shirt",
                "/images/products/product-5.jpg",
                "Shorts",
                109.99,
                74.99),
            new TrendingProduct(
                "Puma Essentials Graphic Tee",
                "/images/products/product-9.jpg",
                "Sweatshirt",
                69.99,
                49.99));
    List<List<TrendingProduct>> partitionedList = partition(trendingProducts, 2);

    model.addAttribute("partitionedList", partitionedList);

    // Shopping Cart: productSuggestion
    model.addAttribute("productSuggestion", trendingProducts);

    // Shopping Cart: cart items
    model.addAttribute("cart", cartService.getOrCreateCart());
  }

  private void addNavbarToModel(Model model) {
    model.addAttribute("categoryList", categoryService.getAllCategories());
  }

  public static <T> List<List<T>> partition(List<T> list, int size) {
    List<List<T>> partitions = new ArrayList<>();
    for (int i = 0; i < list.size(); i += size) {
      partitions.add(list.subList(i, Math.min(i + size, list.size())));
    }
    return partitions;
  }
}
