package com.custom.ngow.shop.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController extends BaseController {

  @GetMapping("/detail")
  public String detail(Model model) {
    addDefaultToModel(model);
    return "view/pages/product-detail";
  }

}
