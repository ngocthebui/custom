package com.custom.ngow.shop.page.shop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.custom.ngow.shop.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController extends BaseController {

  private final ProductService productService;

  @GetMapping
  public String getAllProducts(Model model) {
    addDefaultToModel(model);
    return "view/shop/pages/products";
  }

  @GetMapping("/{sku}")
  public String getProductDetail(@PathVariable String sku, Model model) {
    model.addAttribute("productDetail", productService.getProductDetailBySku(sku));
    addDefaultToModel(model);
    return "view/shop/pages/product-detail";
  }
}
