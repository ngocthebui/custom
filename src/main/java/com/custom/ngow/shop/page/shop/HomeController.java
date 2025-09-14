package com.custom.ngow.shop.page.shop;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.custom.ngow.shop.common.MessageUtil;
import com.custom.ngow.shop.constant.ProductBadge;
import com.custom.ngow.shop.dto.ProductColorDto;
import com.custom.ngow.shop.dto.ProductDto;
import com.custom.ngow.shop.dto.ProductImageDto;
import com.custom.ngow.shop.dto.UserRegistration;
import com.custom.ngow.shop.entity.Banner;
import com.custom.ngow.shop.entity.Category;
import com.custom.ngow.shop.entity.ProductSize;
import com.custom.ngow.shop.service.SearchHistoryCompositeService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController extends BaseController {

  private final MessageUtil messageUtil;

  private final SearchHistoryCompositeService searchHistoryCompositeService;

  @GetMapping
  public String home(Model model) {
    addDefaultToModel(model);

    setBannersToModel(model);
    setPromotionalProductsToModel(model);
    return "view/shop/pages/index";
  }

  @GetMapping("/my-account")
  public String myAccount(Model model) {
    addDefaultToModel(model);

    return "view/shop/pages/account-setting";
  }

  @GetMapping("/faq")
  public String faq() {
    return "view/shop/pages/faq";
  }

  @GetMapping("/wish-list")
  public String wishList() {
    return "view/shop/pages/wishlist";
  }

  @GetMapping("/login")
  public String login(
      @RequestParam(value = "error", required = false) String error,
      @RequestParam(value = "logout", required = false) String logout,
      @RequestParam(value = "expired", required = false) String expired,
      Model model) {
    if (error != null) {
      model.addAttribute("errorMessage", messageUtil.getMessage(error, new String[] {"Account"}));
    }
    if (logout != null) {
      model.addAttribute("successMessage", messageUtil.getMessage("success.logout"));
    }
    if (expired != null) {
      model.addAttribute("expiredMessage", "Phiên đăng nhập đã hết hạn!");
    }

    addDefaultToModel(model);
    return "view/shop/pages/login";
  }

  @GetMapping("/register")
  public String register(
      @ModelAttribute("userRegistration") UserRegistration userRegistration, Model model) {
    addDefaultToModel(model);
    return "view/shop/pages/register";
  }

  @GetMapping("/search")
  public String search(@RequestParam("q") String query, HttpSession session, Model model) {
    searchHistoryCompositeService.saveQuery(query);

    return "redirect:/";
  }

  private void setBannersToModel(Model model) {
    // sliders
    List<Banner> bannerSlides =
        Arrays.asList(
            new Banner(
                1L,
                "Fall Winter Collection",
                "Vivamus lacinia odio vitae vestibulum vestibulum.",
                "/images/slider/slider-1.jpg",
                "shop-default-list.html",
                true,
                0),
            new Banner(
                2L,
                "Spring Summer Collection",
                "Discover the elegance of renewal with soft tones and flowing textures.",
                "/images/slider/slider-2.jpg",
                "shop-default-list.html",
                true,
                1),
            new Banner(
                3L,
                "Urban Edge Series",
                "Bold cuts and minimalist design for the modern city lifestyle.",
                "/images/slider/slider-3.jpg",
                "shop-default-list.html",
                true,
                2));
    model.addAttribute("bannerSlides", bannerSlides);
  }

  private void setPromotionalProductsToModel(Model model) {
    List<List<ProductDto>> promotionalProducts = new ArrayList<>();
    promotionalProducts.add(getBestSellerProducts());
    promotionalProducts.add(getBestSellerProducts());
    promotionalProducts.add(getBestSellerProducts());

    model.addAttribute("promotionalProducts", promotionalProducts);
  }

  private List<ProductDto> getBestSellerProducts() {
    // Product 1
    // images
    Set<ProductImageDto> images1 =
        Set.of(
            new ProductImageDto(
                1L, "/images/products/underwear/product-1.jpg", "Product", null, null, null, null),
            new ProductImageDto(
                2L, "/images/products/underwear/product-2.jpg", "Product", null, null, null, null));
    // sizes
    Set<ProductSize> sizes1 = new HashSet<>();
    // colors
    Set<ProductColorDto> colors1 =
        Set.of(
            new ProductColorDto(
                1L, "Sage Green", "#71A46E", List.of("/images/products/underwear/product-1.jpg")),
            new ProductColorDto(
                2L,
                "Light Orange",
                "#F55B31",
                List.of("/images/products/underwear/product-3.jpg")));

    // Product 2
    // images
    Set<ProductImageDto> images2 =
        Set.of(
            new ProductImageDto(
                1L, "/images/products/underwear/product-4.jpg", "Product", null, null, null, null),
            new ProductImageDto(
                2L, "/images/products/underwear/product-5.jpg", "Product", null, null, null, null));
    // sizes
    Set<ProductSize> sizes2 = new HashSet<>();
    // colors
    Set<ProductColorDto> colors2 =
        Set.of(
            new ProductColorDto(
                1L, "Beige", "#EEDFC6", List.of("/images/products/underwear/product-4.jpg")),
            new ProductColorDto(
                2L, "Dark", "#201E21", List.of("/images/products/underwear/product-6.jpg")),
            new ProductColorDto(
                3L, "Green", "#114842", List.of("/images/products/underwear/product-7.jpg")));

    // Product 3
    // images
    Set<ProductImageDto> images3 =
        Set.of(
            new ProductImageDto(
                1L, "/images/products/underwear/product-8.jpg", "Product", null, null, null, null),
            new ProductImageDto(
                2L, "/images/products/underwear/product-9.jpg", "Product", null, null, null, null));
    // sizes
    Set<ProductSize> sizes3 =
        Set.of(
            new ProductSize(1L, "XS", null, null, null),
            new ProductSize(2L, "S", null, null, null),
            new ProductSize(3L, "M", null, null, null));
    // colors
    Set<ProductColorDto> colors3 =
        Set.of(
            new ProductColorDto(
                1L, "Dusty Olive", "#A6AB84", List.of("/images/products/underwear/product-8.jpg")),
            new ProductColorDto(
                2L,
                "Light Orange",
                "#F55B31",
                List.of("/images/products/underwear/product-10.jpg")),
            new ProductColorDto(
                3L, "Green", "#114842", List.of("/images/products/underwear/product-11.jpg")));

    // Product 4
    // images
    Set<ProductImageDto> images4 =
        Set.of(
            new ProductImageDto(
                1L, "/images/products/underwear/product-12.jpg", "Product", null, null, null, null),
            new ProductImageDto(
                2L,
                "/images/products/underwear/product-13.jpg",
                "Product",
                null,
                null,
                null,
                null));
    // sizes
    Set<ProductSize> sizes4 = new HashSet<>();
    // colors
    Set<ProductColorDto> colors4 =
        Set.of(
            new ProductColorDto(
                1L, "Green", "#114842", List.of("/images/products/underwear/product-12.jpg")),
            new ProductColorDto(
                2L,
                "Dusty Olive",
                "#A6AB84",
                List.of("/images/products/underwear/product-14.jpg")));

    // Product 5
    // images
    Set<ProductImageDto> images5 =
        Set.of(
            new ProductImageDto(
                2L, "/images/products/underwear/product-15.jpg", "Product", null, null, null, null),
            new ProductImageDto(
                1L,
                "/images/products/underwear/product-16.jpg",
                "Product",
                null,
                null,
                null,
                null));
    // sizes
    Set<ProductSize> sizes5 =
        Set.of(
            new ProductSize(1L, "XS", null, null, null),
            new ProductSize(2L, "M", null, null, null));
    // colors
    Set<ProductColorDto> colors5 =
        Set.of(
            new ProductColorDto(
                1L,
                "White",
                "rgba(var(--bs-white-rgb), var(--bs-bg-opacity))",
                List.of("/images/products/underwear/product-15.jpg")),
            new ProductColorDto(
                2L,
                "Light Orange",
                "#F55B31",
                List.of("/images/products/underwear/product-17.jpg")),
            new ProductColorDto(
                3L, "Dark", "#201E21", List.of("/images/products/underwear/product-18.jpg")));

    // Product 6
    // images
    Set<ProductImageDto> images6 =
        Set.of(
            new ProductImageDto(
                1L, "/images/products/underwear/product-19.jpg", "Product", null, null, null, null),
            new ProductImageDto(
                2L,
                "/images/products/underwear/product-20.jpg",
                "Product",
                null,
                null,
                null,
                null));
    // sizes
    Set<ProductSize> sizes6 = new HashSet<>();
    // colors
    Set<ProductColorDto> colors6 =
        Set.of(
            new ProductColorDto(
                1L, "Beige", "#EEDFC6", List.of("/images/products/underwear/product-19.jpg")));

    // Product 7
    // images
    Set<ProductImageDto> images7 =
        Set.of(
            new ProductImageDto(
                1L, "/images/products/underwear/product-21.jpg", "Product", null, null, null, null),
            new ProductImageDto(
                2L,
                "/images/products/underwear/product-22.jpg",
                "Product",
                null,
                null,
                null,
                null));
    // sizes
    Set<ProductSize> sizes7 =
        Set.of(
            new ProductSize(1L, "XS", null, null, null),
            new ProductSize(3L, "S", null, null, null),
            new ProductSize(2L, "M", null, null, null));
    // colors
    Set<ProductColorDto> colors7 =
        Set.of(
            new ProductColorDto(
                1L, "Purple", "#D5D4FE", List.of("/images/products/underwear/product-21.jpg")),
            new ProductColorDto(
                2L, "Dark Violet", "#51518D", List.of("/images/products/underwear/product-23.jpg")),
            new ProductColorDto(
                3L, "Sage Green", "#71A46E", List.of("/images/products/underwear/product-24.jpg")),
            new ProductColorDto(
                4L, "Hot Pink", "#D5D4FE", List.of("/images/products/underwear/product-25.jpg")));

    // Product 8
    // images
    Set<ProductImageDto> images8 =
        Set.of(
            new ProductImageDto(
                1L, "/images/products/underwear/product-26.jpg", "Product", null, null, null, null),
            new ProductImageDto(
                2L,
                "/images/products/underwear/product-27.jpg",
                "Product",
                null,
                null,
                null,
                null));
    // sizes
    Set<ProductSize> sizes8 =
        Set.of(
            new ProductSize(1L, "XS", null, null, null),
            new ProductSize(2L, "S", null, null, null),
            new ProductSize(3L, "M", null, null, null),
            new ProductSize(4L, "L", null, null, null));
    // colors
    Set<ProductColorDto> colors8 =
        Set.of(
            new ProductColorDto(
                1L, "Dark", "#201E21", List.of("/images/products/underwear/product-26.jpg")),
            new ProductColorDto(
                2L, "Purple", "#D5D4FE", List.of("/images/products/underwear/product-28.jpg")),
            new ProductColorDto(
                3L, "Sage Green", "#71A46E", List.of("/images/products/underwear/product-29.jpg")),
            new ProductColorDto(
                4L,
                "White",
                "rgba(var(--bs-white-rgb), var(--bs-bg-opacity))",
                List.of("/images/products/underwear/product-30.jpg")));

    ProductDto productDto1 = new ProductDto();
    productDto1.setName("Seamless breathable thong");
    productDto1.setPrice(BigDecimal.valueOf(99.99));
    productDto1.setSalePrice(BigDecimal.valueOf(69.99));
    productDto1.setImages(images1);
    productDto1.setSizes(sizes1);
    productDto1.setBadge(ProductBadge.SALE);
    productDto1.setColors(colors1);
    productDto1.setCountdownTimer(25472);
    productDto1.setIsTopSale(false);
    productDto1.setCategories(Set.of(new Category(1L, "Test", "test", "", null, null, null)));

    ProductDto productDto2 = new ProductDto();
    productDto2.setName("Cotton high waisted panties");
    productDto2.setPrice(BigDecimal.valueOf(89.99));
    productDto2.setSalePrice(BigDecimal.valueOf(59.99));
    productDto2.setImages(images2);
    productDto2.setSizes(sizes2);
    productDto2.setBadge(null);
    productDto2.setColors(colors2);
    productDto2.setCountdownTimer(0);
    productDto2.setIsTopSale(true);
    productDto2.setCategories(Set.of(new Category(1L, "Test", "test", "", null, null, null)));

    ProductDto productDto3 = new ProductDto();
    productDto3.setName("Sexy lace panties");
    productDto3.setPrice(BigDecimal.valueOf(79.99));
    productDto3.setSalePrice(BigDecimal.valueOf(49.99));
    productDto3.setImages(images3);
    productDto3.setSizes(sizes3);
    productDto3.setBadge(ProductBadge.HOT);
    productDto3.setColors(colors3);
    productDto3.setCountdownTimer(0);
    productDto3.setIsTopSale(true);
    productDto3.setCategories(Set.of(new Category(1L, "Test", "test", "", null, null, null)));

    ProductDto productDto4 = new ProductDto();
    productDto4.setName("Seamless underwear");
    productDto4.setPrice(BigDecimal.valueOf(109.99));
    productDto4.setSalePrice(BigDecimal.valueOf(74.99));
    productDto4.setImages(images4);
    productDto4.setSizes(sizes4);
    productDto4.setBadge(null);
    productDto4.setColors(colors4);
    productDto4.setCountdownTimer(25472);
    productDto4.setIsTopSale(false);
    productDto4.setCategories(Set.of(new Category(1L, "Test", "test", "", null, null, null)));

    ProductDto productDto5 = new ProductDto();
    productDto5.setName("Half sleeve crop top");
    productDto5.setPrice(BigDecimal.valueOf(119.99));
    productDto5.setSalePrice(BigDecimal.valueOf(84.99));
    productDto5.setImages(images5);
    productDto5.setSizes(sizes5);
    productDto5.setBadge(ProductBadge.FLASH_SALE);
    productDto5.setColors(colors5);
    productDto5.setCountdownTimer(0);
    productDto5.setIsTopSale(false);
    productDto5.setCategories(Set.of(new Category(1L, "Test", "test", "", null, null, null)));

    ProductDto productDto6 = new ProductDto();
    productDto6.setName("Elastic waist panties");
    productDto6.setPrice(BigDecimal.valueOf(139.99));
    productDto6.setSalePrice(BigDecimal.valueOf(94.99));
    productDto6.setImages(images6);
    productDto6.setSizes(sizes6);
    productDto6.setBadge(ProductBadge.LIMITED);
    productDto6.setColors(colors6);
    productDto6.setCountdownTimer(0);
    productDto6.setIsTopSale(false);
    productDto6.setCategories(Set.of(new Category(1L, "Test", "test", "", null, null, null)));

    ProductDto productDto7 = new ProductDto();
    productDto7.setName("Seamless breathable thong");
    productDto7.setPrice(BigDecimal.valueOf(69.99));
    productDto7.setSalePrice(BigDecimal.valueOf(44.99));
    productDto7.setImages(images7);
    productDto7.setSizes(sizes7);
    productDto7.setBadge(ProductBadge.NEW);
    productDto7.setColors(colors7);
    productDto7.setCountdownTimer(0);
    productDto7.setIsTopSale(true);
    productDto7.setCategories(Set.of(new Category(1L, "Test", "test", "", null, null, null)));

    ProductDto productDto8 = new ProductDto();
    productDto8.setName("Cotton high waisted panties");
    productDto8.setPrice(BigDecimal.valueOf(129.99));
    productDto8.setSalePrice(BigDecimal.valueOf(84.99));
    productDto8.setImages(images8);
    productDto8.setSizes(sizes8);
    productDto8.setBadge(null);
    productDto8.setColors(colors8);
    productDto8.setCountdownTimer(0);
    productDto8.setIsTopSale(true);
    productDto8.setCategories(Set.of(new Category(1L, "Test", "test", "", null, null, null)));

    // add to list
    return List.of(
        productDto1,
        productDto2,
        productDto3,
        productDto4,
        productDto5,
        productDto6,
        productDto7,
        productDto8);
  }
}
