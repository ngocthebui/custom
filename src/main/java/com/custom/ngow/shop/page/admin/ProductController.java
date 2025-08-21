package com.custom.ngow.shop.page.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.custom.ngow.shop.common.MessageUtil;
import com.custom.ngow.shop.dto.ProductRegistration;
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

  @GetMapping("/update")
  public String updateProduct(
      @ModelAttribute("productDto") ProductRegistration productDto, Model model) {
    model.addAttribute("categoryList", categoryService.getAllCategories());
    model.addAttribute("mode", "Create");
    model.addAttribute("adminDto", userService.getCurrentUserDto());
    return "view/admin/product";
  }

  @PostMapping("/create")
  public String createProduct(
      @Valid @ModelAttribute("productDto") ProductRegistration productDto,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("categoryList", categoryService.getAllCategories());
      model.addAttribute("mode", "Create");
      model.addAttribute("adminDto", userService.getCurrentUserDto());
      model.addAttribute("productDto", productDto);

      handleAllErrorsMessage(bindingResult, model);
      return "view/admin/product";
    }
    redirectAttributes.addFlashAttribute(
        "successMessage", messageUtil.getMessage("success.created", new String[] {"product"}));
    return "redirect:/admin/products/update";
  }

  private void handleAllErrorsMessage(BindingResult bindingResult, Model model) {
    List<String> allErrorsMessage = new ArrayList<>();
    for (ObjectError error : bindingResult.getAllErrors()) {
      allErrorsMessage.add(error.getDefaultMessage());
    }

    model.addAttribute("allErrorsMessage", allErrorsMessage);
  }
}
