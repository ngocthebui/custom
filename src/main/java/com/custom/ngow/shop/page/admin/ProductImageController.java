package com.custom.ngow.shop.page.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.custom.ngow.shop.common.MessageUtil;
import com.custom.ngow.shop.dto.ProductImageListDto;
import com.custom.ngow.shop.entity.Product;
import com.custom.ngow.shop.exception.CustomException;
import com.custom.ngow.shop.service.ProductColorService;
import com.custom.ngow.shop.service.ProductImageService;
import com.custom.ngow.shop.service.ProductService;
import com.custom.ngow.shop.service.ProductSizeService;
import com.custom.ngow.shop.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin/products/images")
@RequiredArgsConstructor
@Slf4j
public class ProductImageController {

  private final MessageUtil messageUtil;
  private final UserService userService;
  private final ProductImageService productImageService;
  private final ProductColorService productColorService;
  private final ProductSizeService productSizeService;
  private final ProductService productService;

  @GetMapping
  public String updateProductImage(@RequestParam("productId") Long productId, Model model) {
    model.addAttribute(
        "imageListDto",
        new ProductImageListDto(
            productId, productImageService.getImagesByProductId(productId).stream().toList()));
    model.addAttribute("colorList", productColorService.getByProductId(productId));
    model.addAttribute("sizeList", productSizeService.getByProductId(productId));
    model.addAttribute("adminDto", userService.getCurrentUserDto());
    return "view/admin/product-image";
  }

  @PostMapping("/register")
  public String updateProductImage(
      @ModelAttribute("imageListDto") ProductImageListDto imageListDto,
      Model model,
      RedirectAttributes redirectAttributes) {
    try {
      Product product = productService.getProductById(imageListDto.getProductId());
      productImageService.saveImagesForProduct(imageListDto, product);
    } catch (CustomException e) {
      model.addAttribute("imageListDto", imageListDto);
      model.addAttribute(
          "colorList", productColorService.getByProductId(imageListDto.getProductId()));
      model.addAttribute(
          "sizeList", productSizeService.getByProductId(imageListDto.getProductId()));
      model.addAttribute("adminDto", userService.getCurrentUserDto());

      model.addAttribute("errorMessage", e.getMessage());
    }

    redirectAttributes.addFlashAttribute(
        "successMessage", messageUtil.getMessage("success.created", new String[] {"images"}));
    return "redirect:/admin/products/images?productId=" + imageListDto.getProductId();
  }
}
