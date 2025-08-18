package com.custom.ngow.shop.page.shop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.custom.ngow.shop.common.MessageUtil;
import com.custom.ngow.shop.constant.ProductBadge;
import com.custom.ngow.shop.dto.ProductDto;
import com.custom.ngow.shop.dto.UserRegistration;
import com.custom.ngow.shop.entity.Banner;
import com.custom.ngow.shop.entity.ProductColor;
import com.custom.ngow.shop.entity.ProductImage;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController extends BaseController {

  private final MessageUtil messageUtil;

  @GetMapping
  public String home(Model model) {
    addDefaultToModel(model);

    setBannersToModel(model);
    setPromotionalProductsToModel(model);
    return "view/pages/index";
  }

  @GetMapping("/my-account")
  public String myAccount(Model model) {
    addDefaultToModel(model);

    return "view/pages/account-setting";
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
    return "view/pages/login";
  }

  @GetMapping("/register")
  public String register(
      @ModelAttribute("userRegistration") UserRegistration userRegistration, Model model) {
    addDefaultToModel(model);
    return "view/pages/register";
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
    promotionalProducts.add(getTrendingProducts());
    promotionalProducts.add(getBestSellerProducts());
    promotionalProducts.add(getTrendingProducts());

    model.addAttribute("promotionalProducts", promotionalProducts);
  }

  private List<ProductDto> getBestSellerProducts() {
    // Product 1
    // images
    List<ProductImage> images1 =
        List.of(
            new ProductImage(
                1L,
                "/images/products/underwear/product-1.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null),
            new ProductImage(
                1L,
                "/images/products/underwear/product-2.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null));
    // sizes
    List<String> sizes1 = new ArrayList<>();
    // badges
    List<ProductBadge> badges1 = List.of(ProductBadge.SALE);
    // colors
    List<ProductColor> colors1 =
        List.of(
            new ProductColor(
                1L,
                "Sage Green",
                "#71A46E",
                "/images/products/underwear/product-1.jpg",
                true,
                null),
            new ProductColor(
                1L,
                "Light Orange",
                "#F55B31",
                "/images/products/underwear/product-3.jpg",
                true,
                null));

    // Product 2
    // images
    List<ProductImage> images2 =
        List.of(
            new ProductImage(
                1L,
                "/images/products/underwear/product-4.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null),
            new ProductImage(
                1L,
                "/images/products/underwear/product-5.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null));
    // sizes
    List<String> sizes2 = new ArrayList<>();
    // badges
    List<ProductBadge> badges2 = new ArrayList<>();
    // colors
    List<ProductColor> colors2 =
        List.of(
            new ProductColor(
                1L, "Beige", "#EEDFC6", "/images/products/underwear/product-4.jpg", true, null),
            new ProductColor(
                1L, "Dark", "#201E21", "/images/products/underwear/product-6.jpg", true, null),
            new ProductColor(
                1L, "Green", "#114842", "/images/products/underwear/product-7.jpg", true, null));

    // Product 3
    // images
    List<ProductImage> images3 =
        List.of(
            new ProductImage(
                1L,
                "/images/products/underwear/product-8.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null),
            new ProductImage(
                1L,
                "/images/products/underwear/product-9.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null));
    // sizes
    List<String> sizes3 = Arrays.asList("XS", "S", "M");
    // badges
    List<ProductBadge> badges3 = List.of(ProductBadge.HOT);
    // colors
    List<ProductColor> colors3 =
        List.of(
            new ProductColor(
                1L,
                "Dusty Olive",
                "#A6AB84",
                "/images/products/underwear/product-8.jpg",
                true,
                null),
            new ProductColor(
                1L,
                "Light Orange",
                "#F55B31",
                "/images/products/underwear/product-10.jpg",
                true,
                null),
            new ProductColor(
                1L, "Green", "#114842", "/images/products/underwear/product-11.jpg", true, null));

    // Product 4
    // images
    List<ProductImage> images4 =
        List.of(
            new ProductImage(
                1L,
                "/images/products/underwear/product-12.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null),
            new ProductImage(
                1L,
                "/images/products/underwear/product-13.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null));
    // sizes
    List<String> sizes4 = new ArrayList<>();
    // badges
    List<ProductBadge> badges4 = new ArrayList<>();
    // colors
    List<ProductColor> colors4 =
        List.of(
            new ProductColor(
                1L, "Green", "#114842", "/images/products/underwear/product-12.jpg", true, null),
            new ProductColor(
                1L,
                "Dusty Olive",
                "#A6AB84",
                "/images/products/underwear/product-14.jpg",
                true,
                null));

    // Product 5
    // images
    List<ProductImage> images5 =
        List.of(
            new ProductImage(
                1L,
                "/images/products/underwear/product-15.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null),
            new ProductImage(
                1L,
                "/images/products/underwear/product-16.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null));
    // sizes
    List<String> sizes5 = List.of("XS", "M");
    // badges
    List<ProductBadge> badges5 = List.of(ProductBadge.FLASH_SALE);
    // colors
    List<ProductColor> colors5 =
        List.of(
            new ProductColor(
                1L,
                "White",
                "rgba(var(--bs-white-rgb), var(--bs-bg-opacity))",
                "/images/products/underwear/product-15.jpg",
                true,
                null),
            new ProductColor(
                1L,
                "Light Orange",
                "#F55B31",
                "/images/products/underwear/product-17.jpg",
                true,
                null),
            new ProductColor(
                1L, "Dark", "#201E21", "/images/products/underwear/product-18.jpg", true, null));

    // Product 6
    // images
    List<ProductImage> images6 =
        List.of(
            new ProductImage(
                1L,
                "/images/products/underwear/product-19.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null),
            new ProductImage(
                1L,
                "/images/products/underwear/product-20.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null));
    // sizes
    List<String> sizes6 = new ArrayList<>();
    // badges
    List<ProductBadge> badges6 = List.of(ProductBadge.LIMITED);
    // colors
    List<ProductColor> colors6 =
        List.of(
            new ProductColor(
                1L, "Beige", "#EEDFC6", "/images/products/underwear/product-19.jpg", true, null));

    // Product 7
    // images
    List<ProductImage> images7 =
        List.of(
            new ProductImage(
                1L,
                "/images/products/underwear/product-21.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null),
            new ProductImage(
                1L,
                "/images/products/underwear/product-22.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null));
    // sizes
    List<String> sizes7 = List.of("XS", "S", "M");
    // badges
    List<ProductBadge> badges7 = List.of(ProductBadge.NEW);
    // colors
    List<ProductColor> colors7 =
        List.of(
            new ProductColor(
                1L, "Purple", "#D5D4FE", "/images/products/underwear/product-21.jpg", true, null),
            new ProductColor(
                1L,
                "Dark Violet",
                "#51518D",
                "/images/products/underwear/product-23.jpg",
                true,
                null),
            new ProductColor(
                1L,
                "Sage Green",
                "#71A46E",
                "/images/products/underwear/product-24.jpg",
                true,
                null),
            new ProductColor(
                1L,
                "Hot Pink",
                "#D5D4FE",
                "/images/products/underwear/product-25.jpg",
                true,
                null));

    // Product 8
    // images
    List<ProductImage> images8 =
        List.of(
            new ProductImage(
                1L,
                "/images/products/underwear/product-26.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null),
            new ProductImage(
                1L,
                "/images/products/underwear/product-27.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null));
    // sizes
    List<String> sizes8 = List.of("XS", "S", "M", "L");
    // badges
    List<ProductBadge> badges8 = new ArrayList<>();
    // colors
    List<ProductColor> colors8 =
        List.of(
            new ProductColor(
                1L, "Dark", "#201E21", "/images/products/underwear/product-26.jpg", true, null),
            new ProductColor(
                1L, "Purple", "#D5D4FE", "/images/products/underwear/product-28.jpg", true, null),
            new ProductColor(
                1L,
                "Sage Green",
                "#71A46E",
                "/images/products/underwear/product-29.jpg",
                true,
                null),
            new ProductColor(
                1L,
                "White",
                "rgba(var(--bs-white-rgb), var(--bs-bg-opacity))",
                "/images/products/underwear/product-30.jpg",
                true,
                null));

    // add to list
    return List.of(
        new ProductDto(
            1L,
            "Seamless breathable thong",
            99.99,
            69.99,
            images1,
            sizes1,
            badges1,
            colors1,
            25472,
            false),
        new ProductDto(
            2L,
            "Cotton high waisted panties",
            89.99,
            59.99,
            images2,
            sizes2,
            badges2,
            colors2,
            0,
            true),
        new ProductDto(
            3L, "Sexy lace panties", 79.99, 49.99, images3, sizes3, badges3, colors3, 0, true),
        new ProductDto(
            4L,
            "Seamless underwear",
            109.99,
            74.99,
            images4,
            sizes5,
            badges4,
            colors4,
            25472,
            false),
        new ProductDto(
            5L, "Half sleeve crop top", 119.99, 84.99, images5, sizes5, badges5, colors5, 0, false),
        new ProductDto(
            6L,
            "Elastic waist panties",
            139.99,
            94.99,
            images6,
            sizes6,
            badges6,
            colors6,
            0,
            false),
        new ProductDto(
            7L,
            "Seamless breathable thong",
            69.99,
            44.99,
            images7,
            sizes7,
            badges7,
            colors7,
            0,
            true),
        new ProductDto(
            8L,
            "Cotton high waisted panties",
            129.99,
            89.99,
            images8,
            sizes8,
            badges8,
            colors8,
            0,
            true));
  }

  private List<ProductDto> getTrendingProducts() {
    // Product 1
    // images
    List<ProductImage> images1 =
        List.of(
            new ProductImage(
                1L,
                "/images/products/product-1.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null),
            new ProductImage(
                1L,
                "/images/products/product-2.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null));
    // sizes
    List<String> sizes1 = Arrays.asList("XS", "S", "M");
    // badges
    List<ProductBadge> badges1 = List.of(ProductBadge.HOT);
    // colors
    List<ProductColor> colors1 =
        List.of(
            new ProductColor(1L, "Pink", "#F56EB7", "/images/products/product-7.jpg", true, null),
            new ProductColor(1L, "Geige", "#EEDFC6", "/images/products/product-9.jpg", true, null),
            new ProductColor(
                1L, "Olive", "#A6AB84", "/images/products/product-11.jpg", true, null));

    // Product 2
    // images
    List<ProductImage> images2 =
        List.of(
            new ProductImage(
                1L,
                "/images/products/product-7.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null),
            new ProductImage(
                1L,
                "/images/products/product-8.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null));
    // sizes
    List<String> sizes2 = new ArrayList<>();
    // badges
    List<ProductBadge> badges2 = List.of(ProductBadge.FLASH_SALE);
    // colors
    List<ProductColor> colors2 =
        List.of(
            new ProductColor(1L, "Brown", "#C8A07C", "/images/products/product-1.jpg", true, null),
            new ProductColor(1L, "Blue", "#B2CBEA", "/images/products/product-4.jpg", true, null),
            new ProductColor(
                1L, "Orange", "#EC6E1A", "/images/products/product-3.jpg", true, null));

    // Product 3
    // images
    List<ProductImage> images3 =
        List.of(
            new ProductImage(
                1L,
                "/images/products/product-13.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null),
            new ProductImage(
                1L,
                "/images/products/product-14.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null));
    // sizes
    List<String> sizes3 = new ArrayList<>();
    // badges
    List<ProductBadge> badges3 = List.of(ProductBadge.TRENDING);
    // colors
    List<ProductColor> colors3 = new ArrayList<>();

    // Product 4
    // images
    List<ProductImage> images4 =
        List.of(
            new ProductImage(
                1L,
                "/images/products/product-23.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null),
            new ProductImage(
                1L,
                "/images/products/product-24.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null));
    // sizes
    List<String> sizes4 = Arrays.asList("XS", "S", "M");
    // badges
    List<ProductBadge> badges4 = List.of(ProductBadge.NEW);
    // colors
    List<ProductColor> colors4 =
        List.of(
            new ProductColor(1L, "Beige", "#EEDFC6", "/images/products/product-23.jpg", true, null),
            new ProductColor(1L, "Dark", "#201E21", "/images/products/product-25.jpg", true, null),
            new ProductColor(
                1L, "Sage Green", "#71A46E", "/images/products/product-26.jpg", true, null));

    // Product 5
    // images
    List<ProductImage> images5 =
        List.of(
            new ProductImage(
                1L,
                "/images/products/product-21.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null),
            new ProductImage(
                1L,
                "/images/products/product-22.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null));
    // sizes
    List<String> sizes5 = Arrays.asList("XS", "M");
    // badges
    List<ProductBadge> badges5 = List.of(ProductBadge.FLASH_SALE);
    // colors
    List<ProductColor> colors5 =
        List.of(
            new ProductColor(1L, "Beige", "#EEDFC6", "/images/products/product-23.jpg", true, null),
            new ProductColor(1L, "Dark", "#201E21", "/images/products/product-25.jpg", true, null),
            new ProductColor(
                1L, "Sage Green", "#71A46E", "/images/products/product-26.jpg", true, null));

    // Product 6
    // images
    List<ProductImage> images6 =
        List.of(
            new ProductImage(
                1L,
                "/images/products/product-33.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null),
            new ProductImage(
                1L,
                "/images/products/product-34.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null));
    // sizes
    List<String> sizes6 = new ArrayList<>();
    // badges
    List<ProductBadge> badges6 = List.of(ProductBadge.SALE);
    // colors
    List<ProductColor> colors6 =
        List.of(
            new ProductColor(1L, "Dark", "#201E21", "/images/products/product-33.jpg", true, null),
            new ProductColor(1L, "Beige", "#EEDFC6", "/images/products/product-35.jpg", true, null),
            new ProductColor(1L, "Jade", "#114842", "/images/products/product-36.jpg", true, null));

    // Product 7
    // images
    List<ProductImage> images7 =
        List.of(
            new ProductImage(
                1L,
                "/images/products/product-37.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null),
            new ProductImage(
                1L,
                "/images/products/product-38.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null));
    // sizes
    List<String> sizes7 = Arrays.asList("XS", "S", "M");
    // badges
    List<ProductBadge> badges7 = List.of(ProductBadge.NEW);
    // colors
    List<ProductColor> colors7 =
        List.of(
            new ProductColor(
                1L,
                "White",
                "rgba(var(--bs-white-rgb), var(--bs-bg-opacity))",
                "/images/products/product-37.jpg",
                true,
                null),
            new ProductColor(1L, "Pink", "#F56EB7", "/images/products/product-39.jpg", true, null),
            new ProductColor(
                1L, "Beige", "#EEDFC6", "/images/products/product-40.jpg", true, null));

    // Product 8
    // images
    List<ProductImage> images8 =
        List.of(
            new ProductImage(
                1L,
                "/images/products/product-41.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null),
            new ProductImage(
                1L,
                "/images/products/product-42.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null,
                null));
    // sizes
    List<String> sizes8 = Arrays.asList("XS", "S", "M", "L");
    // badges
    List<ProductBadge> badges8 = null;
    // colors
    List<ProductColor> colors8 =
        List.of(
            new ProductColor(1L, "Beige", "#EEDFC6", "/images/products/product-41.jpg", true, null),
            new ProductColor(
                1L,
                "Black",
                "rgba(var(--bs-black-rgb), var(--bs-bg-opacity))",
                "/images/products/product-43.jpg",
                true,
                null),
            new ProductColor(
                1L, "Violet", "#51518D", "/images/products/product-44.jpg", true, null));

    // add to list
    return List.of(
        new ProductDto(
            1L, "Summer Two Piece Set", 99.99, 69.99, images1, sizes1, badges1, colors1, 0, false),
        new ProductDto(
            2L,
            "Nike Sportswear Tee Shirts",
            99.99,
            69.99,
            images2,
            sizes2,
            badges2,
            colors2,
            25472,
            false),
        new ProductDto(
            3L,
            "Women's Straight Leg Pants",
            29.99,
            19.99,
            images3,
            sizes3,
            badges3,
            colors3,
            0,
            true),
        new ProductDto(
            4L,
            "V-neck button down vest",
            99.99,
            69.99,
            images4,
            sizes4,
            badges4,
            colors4,
            0,
            false),
        new ProductDto(
            5L, "Half sleeve crop top", 119.99, 84.99, images5, sizes5, badges5, colors5, 0, true),
        new ProductDto(
            6L, "Summer two piece set", 139.99, 94.99, images6, sizes6, badges6, colors6, 0, false),
        new ProductDto(
            7L,
            "Women's straight leg pants",
            69.99,
            44.99,
            images7,
            sizes7,
            badges7,
            colors7,
            0,
            true),
        new ProductDto(
            8L,
            "Short sleeve office shirt",
            129.99,
            89.99,
            images8,
            sizes8,
            badges8,
            colors8,
            0,
            false));
  }
}
