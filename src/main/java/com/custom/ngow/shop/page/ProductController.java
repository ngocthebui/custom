package com.custom.ngow.shop.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.custom.ngow.shop.dto.ProductDto;

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
    return "view/pages/product-detail";
  }

  private ProductDto getProductDetail() {
    ProductDto product = new ProductDto();

    product.setId(1L);
    product.setName("Casual Round Neck T-Shirt");

    return product;
  }
}
