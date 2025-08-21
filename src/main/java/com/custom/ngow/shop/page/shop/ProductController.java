package com.custom.ngow.shop.page.shop;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.custom.ngow.shop.dto.ProductDto;
import com.custom.ngow.shop.dto.ProductImageDto;
import com.custom.ngow.shop.entity.Category;
import com.custom.ngow.shop.entity.ProductColor;
import com.custom.ngow.shop.entity.ProductSize;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController extends BaseController {

  @GetMapping("/detail")
  public String detail(Model model) {

    ProductDto product = getProductDetail();
    model.addAttribute("productDetail", product);

    addDefaultToModel(model);
    return "view/shop/pages/product-detail";
  }

  private ProductDto getProductDetail() {
    ProductDto product = new ProductDto();

    product.setId(1L);
    product.setName("Casual Round Neck T-Shirt");
    product.setImages(getProductImages());
    product.setSizes(getProductSizes());
    product.setColors(getProductColors());
    product.setRating(4.8);
    product.setReviewCount(3671);

    DecimalFormat df = new DecimalFormat("#.##");
    product.setPrice(Double.parseDouble(df.format(19.337)));
    product.setSalePercent(29);
    product.setSalePrice(14.99);

    product.setCountdownTimer(1007500);
    product.setSku("Themesflat_#KT_Yellow_7");
    product.setCategories(getCategories());
    product.setDescription(getDescription());

    product.setMaterial("Da nhân tạo nếp nhăn");
    product.setStrapQuantity(1);
    product.setInnerPocket("1 ngăn phía trong");
    product.setStrapLength("22-64 cm");
    product.setRemovableStrap(false);
    product.setAdjustableStrap(true);
    product.setLockType("Khóa zip");
    product.setHandleLength("22-28.5 cm");
    product.setStrapTotalLength("36-118 cm");
    product.setWeight("272 g");
    product.setWidth("26 cm");
    product.setDepth("6 cm");
    product.setHeight("15 cm");

    return product;
  }

  private List<ProductImageDto> getProductImages() {
    return List.of(
        new ProductImageDto(
            1L, "/images/products/fashion/product-1.jpg", "Image", false, "blue", "XS"),
        new ProductImageDto(
            1L, "/images/products/fashion/product-2.jpg", "Image", false, "blue", "M"),
        new ProductImageDto(
            1L, "/images/products/fashion/product-3.jpg", "Image", false, "blue", "S"),
        new ProductImageDto(
            1L, "/images/products/fashion/product-4.jpg", "Image", false, "blue", "L"),
        new ProductImageDto(
            1L, "/images/products/fashion/product-5.jpg", "Image", false, "blue", "L"),
        new ProductImageDto(
            1L, "/images/products/fashion/product-6.jpg", "Image", false, "gray", "M"),
        new ProductImageDto(
            1L, "/images/products/fashion/product-7.jpg", "Image", false, "pink", "M"),
        new ProductImageDto(
            1L, "/images/products/fashion/product-8.jpg", "Image", false, "green", "M"),
        new ProductImageDto(
            1L, "/images/products/fashion/product-9.jpg", "Image", false, "gray", "M"),
        new ProductImageDto(
            1L, "/images/products/fashion/product-10.jpg", "Image", false, "gray", "M"),
        new ProductImageDto(
            1L, "/images/products/fashion/product-11.jpg", "Image", false, "white", "M"));
  }

  private List<ProductSize> getProductSizes() {
    return List.of(
        new ProductSize(1L, "XS", null, null, null),
        new ProductSize(1L, "S", null, null, null),
        new ProductSize(1L, "M", null, null, null),
        new ProductSize(1L, "L", null, null, null));
  }

  private List<ProductColor> getProductColors() {
    return List.of(
        new ProductColor(1L, "blue", "#B2CBEAFF", null, null, null),
        new ProductColor(1L, "gray", "#C8A07C", null, null, null),
        new ProductColor(1L, "pink", "#F56EB7", null, null, null),
        new ProductColor(1L, "green", "#114842", null, null, null),
        new ProductColor(
            1L, "white", "rgba(var(--bs-white-rgb), var(--bs-bg-opacity))", null, null, null));
  }

  private Set<Category> getCategories() {
    return Set.of(
        new Category(1L, "Daily Wear Rings", null, null, null, null),
        new Category(1L, "Ring diamond", null, null, null, null),
        new Category(1L, "Anniversary rings", null, null, null, null),
        new Category(1L, "Solitaire Rings", null, null, null, null),
        new Category(1L, "Half Eternity Rings", null, null, null, null));
  }

  private String getDescription() {
    return "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec a tortor commodo enim pulvinar hendrerit. Mauris a leo rutrum lectus vehicula dignissim feugiat eu felis. Fusce libero est, commodo vitae ultricies id, sollicitudin a augue. In finibus suscipit nulla, id bibendum diam fermentum sed. Suspendisse potenti. Proin finibus turpis mauris, et fringilla ex scelerisque ut. Nam laoreet pulvinar lacus, eu suscipit justo. Donec nec leo enim. Morbi lacinia varius mi, nec mattis felis rhoncus et. Donec ac facilisis arcu. Mauris tristique lorem id velit mattis finibus. Sed a neque augue. Vestibulum metus lectus, ultricies id rhoncus iaculis, accumsan a lectus. Duis viverra, risus sed egestas blandit, ante libero rutrum tortor, sed dignissim dolor nunc id arcu.";
  }
}
