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
import com.custom.ngow.shop.dto.ProductImageDto;
import com.custom.ngow.shop.dto.UserRegistration;
import com.custom.ngow.shop.entity.Banner;
import com.custom.ngow.shop.entity.ProductColor;
import com.custom.ngow.shop.entity.ProductSize;

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

  @GetMapping("/all-products")
  public String shop5columns() {
    return "view/shop/pages/shop-5-columns";
  }

  @GetMapping("/shop-2-columns")
  public String shop2columns() {
    return "view/shop/pages/shop-2-columns";
  }

  @GetMapping("/product-detail")
  public String productDetail() {
    return "view/shop/pages/product-detail";
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

    return "view/shop/pages/shop-5-columns";
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
    List<ProductImageDto> images1 =
        List.of(
            new ProductImageDto(
                1L,
                "/images/products/underwear/product-1.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null),
            new ProductImageDto(
                1L,
                "/images/products/underwear/product-2.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null));
    // sizes
    List<ProductSize> sizes1 = new ArrayList<>();
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
    List<ProductImageDto> images2 =
        List.of(
            new ProductImageDto(
                1L,
                "/images/products/underwear/product-4.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null),
            new ProductImageDto(
                1L,
                "/images/products/underwear/product-5.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null));
    // sizes
    List<ProductSize> sizes2 = new ArrayList<>();
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
    List<ProductImageDto> images3 =
        List.of(
            new ProductImageDto(
                1L,
                "/images/products/underwear/product-8.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null),
            new ProductImageDto(
                1L,
                "/images/products/underwear/product-9.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null));
    // sizes
    List<ProductSize> sizes3 =
        Arrays.asList(
            new ProductSize(1L, "XS", null, null, null),
            new ProductSize(1L, "S", null, null, null),
            new ProductSize(1L, "M", null, null, null));
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
    List<ProductImageDto> images4 =
        List.of(
            new ProductImageDto(
                1L,
                "/images/products/underwear/product-12.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null),
            new ProductImageDto(
                1L,
                "/images/products/underwear/product-13.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null));
    // sizes
    List<ProductSize> sizes4 = new ArrayList<>();
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
    List<ProductImageDto> images5 =
        List.of(
            new ProductImageDto(
                1L,
                "/images/products/underwear/product-15.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null),
            new ProductImageDto(
                1L,
                "/images/products/underwear/product-16.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null));
    // sizes
    List<ProductSize> sizes5 =
        List.of(
            new ProductSize(1L, "XS", null, null, null),
            new ProductSize(1L, "M", null, null, null));
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
    List<ProductImageDto> images6 =
        List.of(
            new ProductImageDto(
                1L,
                "/images/products/underwear/product-19.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null),
            new ProductImageDto(
                1L,
                "/images/products/underwear/product-20.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null));
    // sizes
    List<ProductSize> sizes6 = new ArrayList<>();
    // colors
    List<ProductColor> colors6 =
        List.of(
            new ProductColor(
                1L, "Beige", "#EEDFC6", "/images/products/underwear/product-19.jpg", true, null));

    // Product 7
    // images
    List<ProductImageDto> images7 =
        List.of(
            new ProductImageDto(
                1L,
                "/images/products/underwear/product-21.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null),
            new ProductImageDto(
                1L,
                "/images/products/underwear/product-22.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null));
    // sizes
    List<ProductSize> sizes7 =
        List.of(
            new ProductSize(1L, "XS", null, null, null),
            new ProductSize(1L, "S", null, null, null),
            new ProductSize(1L, "M", null, null, null));
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
    List<ProductImageDto> images8 =
        List.of(
            new ProductImageDto(
                1L,
                "/images/products/underwear/product-26.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null),
            new ProductImageDto(
                1L,
                "/images/products/underwear/product-27.jpg",
                "Product",
                null,
                null,
                null,
                null,
                null));
    // sizes
    List<ProductSize> sizes8 =
        List.of(
            new ProductSize(1L, "XS", null, null, null),
            new ProductSize(1L, "S", null, null, null),
            new ProductSize(1L, "M", null, null, null),
            new ProductSize(1L, "L", null, null, null));
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

    ProductDto productDto1 = new ProductDto();
    productDto1.setName("Seamless breathable thong");
    productDto1.setPrice(99.99);
    productDto1.setSalePrice(69.99);
    productDto1.setImages(images1);
    productDto1.setSizes(sizes1);
    productDto1.setBadge(ProductBadge.SALE);
    productDto1.setColors(colors1);
    productDto1.setCountdownTimer(25472);
    productDto1.setIsTopSale(false);

    ProductDto productDto2 = new ProductDto();
    productDto2.setName("Cotton high waisted panties");
    productDto2.setPrice(89.99);
    productDto2.setSalePrice(59.99);
    productDto2.setImages(images2);
    productDto2.setSizes(sizes2);
    productDto2.setBadge(null);
    productDto2.setColors(colors2);
    productDto2.setCountdownTimer(0);
    productDto2.setIsTopSale(true);

    ProductDto productDto3 = new ProductDto();
    productDto3.setName("Sexy lace panties");
    productDto3.setPrice(79.99);
    productDto3.setSalePrice(49.99);
    productDto3.setImages(images3);
    productDto3.setSizes(sizes3);
    productDto3.setBadge(ProductBadge.HOT);
    productDto3.setColors(colors3);
    productDto3.setCountdownTimer(0);
    productDto3.setIsTopSale(true);

    ProductDto productDto4 = new ProductDto();
    productDto4.setName("Seamless underwear");
    productDto4.setPrice(109.99);
    productDto4.setSalePrice(74.99);
    productDto4.setImages(images4);
    productDto4.setSizes(sizes4);
    productDto4.setBadge(null);
    productDto4.setColors(colors4);
    productDto4.setCountdownTimer(25472);
    productDto4.setIsTopSale(false);

    ProductDto productDto5 = new ProductDto();
    productDto5.setName("Half sleeve crop top");
    productDto5.setPrice(119.99);
    productDto5.setSalePrice(84.99);
    productDto5.setImages(images5);
    productDto5.setSizes(sizes5);
    productDto5.setBadge(ProductBadge.FLASH_SALE);
    productDto5.setColors(colors5);
    productDto5.setCountdownTimer(0);
    productDto5.setIsTopSale(false);

    ProductDto productDto6 = new ProductDto();
    productDto6.setName("Elastic waist panties");
    productDto6.setPrice(139.99);
    productDto6.setSalePrice(94.99);
    productDto6.setImages(images6);
    productDto6.setSizes(sizes6);
    productDto6.setBadge(ProductBadge.LIMITED);
    productDto6.setColors(colors6);
    productDto6.setCountdownTimer(0);
    productDto6.setIsTopSale(false);

    ProductDto productDto7 = new ProductDto();
    productDto7.setName("Seamless breathable thong");
    productDto7.setPrice(69.99);
    productDto7.setSalePrice(44.99);
    productDto7.setImages(images7);
    productDto7.setSizes(sizes7);
    productDto7.setBadge(ProductBadge.NEW);
    productDto7.setColors(colors7);
    productDto7.setCountdownTimer(0);
    productDto7.setIsTopSale(true);

    ProductDto productDto8 = new ProductDto();
    productDto8.setName("Cotton high waisted panties");
    productDto8.setPrice(129.99);
    productDto8.setSalePrice(84.99);
    productDto8.setImages(images8);
    productDto8.setSizes(sizes8);
    productDto8.setBadge(null);
    productDto8.setColors(colors8);
    productDto8.setCountdownTimer(0);
    productDto8.setIsTopSale(true);

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
