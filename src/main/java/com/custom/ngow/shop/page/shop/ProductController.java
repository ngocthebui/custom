package com.custom.ngow.shop.page.shop;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.custom.ngow.shop.dto.ProductDto;
import com.custom.ngow.shop.dto.ProductImageDto;
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
}
