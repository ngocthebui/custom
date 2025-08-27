package com.custom.ngow.shop.page.admin;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.custom.ngow.shop.common.MessageUtil;
import com.custom.ngow.shop.constant.Mode;
import com.custom.ngow.shop.constant.ProductBadge;
import com.custom.ngow.shop.dto.ProductRegistration;
import com.custom.ngow.shop.entity.Product;
import com.custom.ngow.shop.exception.CustomException;
import com.custom.ngow.shop.service.CategoryService;
import com.custom.ngow.shop.service.ProductService;
import com.custom.ngow.shop.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller("AdminProductController")
@RequestMapping("/admin/products")
@RequiredArgsConstructor
@Service
public class ProductController {

  private final ProductService productService;
  private final MessageUtil messageUtil;
  private final UserService userService;
  private final CategoryService categoryService;

  @GetMapping("/create")
  public String createProduct(
      @ModelAttribute("productDto") ProductRegistration productDto, Model model) {
    model.addAttribute("categoryList", categoryService.getAllCategories());
    model.addAttribute("badgeList", ProductBadge.values());
    model.addAttribute("mode", Mode.CREATE.name());
    model.addAttribute("adminDto", userService.getCurrentUserDto());
    return "view/admin/product";
  }

  @GetMapping("/update")
  public String updateProduct(@RequestParam("productId") Long productId, Model model) {
    model.addAttribute("productDto", productService.getProductForUpdate(productId));
    model.addAttribute("categoryList", categoryService.getAllCategories());
    model.addAttribute("badgeList", ProductBadge.values());
    model.addAttribute("mode", Mode.UPDATE.name());
    model.addAttribute("adminDto", userService.getCurrentUserDto());
    return "view/admin/product";
  }

  @PostMapping("/register")
  @Transactional
  public String registerProduct(
      @Valid @ModelAttribute("productDto") ProductRegistration productDto,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    model.addAttribute("mode", productDto.getMode());
    if (bindingResult.hasErrors()) {
      model.addAttribute("categoryList", categoryService.getAllCategories());
      model.addAttribute("badgeList", ProductBadge.values());
      model.addAttribute("adminDto", userService.getCurrentUserDto());
      model.addAttribute("productDto", productDto);
      return "view/admin/product";
    }

    Product product;
    String successCode;
    try {
      if (productDto.getId() != null) {
        product = productService.updateProduct(productDto.getId(), productDto);
        successCode = "success.updated";
      } else {
        product = productService.createProduct(productDto);
        successCode = "success.created";
      }
    } catch (CustomException e) {
      model.addAttribute("categoryList", categoryService.getAllCategories());
      model.addAttribute("badgeList", ProductBadge.values());
      model.addAttribute("adminDto", userService.getCurrentUserDto());
      model.addAttribute("productDto", productDto);

      model.addAttribute("errorMessage", e.getMessage());
      return "view/admin/product";
    }

    redirectAttributes.addFlashAttribute(
        "successMessage", messageUtil.getMessage(successCode, new String[] {"product"}));
    return "redirect:/admin/products/images?productId=" + product.getId();
  }
}
