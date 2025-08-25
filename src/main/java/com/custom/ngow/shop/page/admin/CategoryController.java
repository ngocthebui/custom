package com.custom.ngow.shop.page.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.custom.ngow.shop.common.MessageUtil;
import com.custom.ngow.shop.dto.CategoryDto;
import com.custom.ngow.shop.exception.CustomException;
import com.custom.ngow.shop.service.CategoryService;
import com.custom.ngow.shop.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

  private static final int PAGE_SIZE = 5;

  private final UserService userService;
  private final CategoryService categoryService;
  private final MessageUtil messageUtil;

  @GetMapping
  public String categories(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "id") String sort,
      @RequestParam(defaultValue = "asc") String dir,
      Model model) {
    model.addAttribute("adminDto", userService.getCurrentUserDto());

    model.addAttribute("countTotalCategories", categoryService.countAllCategories());

    model.addAttribute("pageData", categoryService.getCategories(page, PAGE_SIZE, sort, dir));
    model.addAttribute("pagingUrl", "/admin/categories");
    model.addAttribute("sort", sort);
    model.addAttribute("dir", dir);
    model.addAttribute("reverseDir", dir.equals("asc") ? "desc" : "asc");
    return "view/admin/categories";
  }

  @GetMapping("/update")
  public String preCreateCategory(
      @ModelAttribute("categoryDto") CategoryDto categoryDto, Model model) {
    model.addAttribute("mode", "Create");
    model.addAttribute("adminDto", userService.getCurrentUserDto());
    return "view/admin/category-create";
  }

  @PostMapping("/create")
  public String createCategory(
      @ModelAttribute("categoryDto") CategoryDto categoryDto,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("mode", "Create");
      model.addAttribute("adminDto", userService.getCurrentUserDto());
      model.addAttribute("categoryDto", categoryDto);
      return "view/admin/category-create";
    }

    try {
      categoryService.createCategory(categoryDto);
    } catch (CustomException e) {
      model.addAttribute("mode", "Create");
      model.addAttribute("adminDto", userService.getCurrentUserDto());
      model.addAttribute("categoryDto", categoryDto);
      model.addAttribute("errorMessage", e.getMessage());
      return "view/admin/category-create";
    }

    redirectAttributes.addFlashAttribute(
        "successMessage", messageUtil.getMessage("success.created", new String[] {"category"}));
    return "redirect:/admin/categories/update";
  }
}
