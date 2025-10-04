package com.custom.ngow.shop.page.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.custom.ngow.shop.common.MessageUtil;
import com.custom.ngow.shop.dto.BannerDto;
import com.custom.ngow.shop.entity.Banner;
import com.custom.ngow.shop.exception.CustomException;
import com.custom.ngow.shop.security.BannerService;
import com.custom.ngow.shop.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/banners")
@RequiredArgsConstructor
public class BannerController {

  private static final int PAGE_SIZE = 5;

  private final UserService userService;
  private final BannerService bannerService;
  private final MessageUtil messageUtil;

  @GetMapping
  public String getBanners(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "id") String sort,
      @RequestParam(defaultValue = "asc") String dir,
      Model model) {
    model.addAttribute("countActiveBanners", bannerService.countByIsActive(true));
    model.addAttribute("countInactiveBanners", bannerService.countByIsActive(false));
    model.addAttribute("countTotalBanners", bannerService.countAllBanner());

    model.addAttribute("pageData", bannerService.getAllBanners(page, PAGE_SIZE, sort, dir));
    model.addAttribute("adminDto", userService.getCurrentUserDto());

    model.addAttribute("pagingUrl", "/admin/banners");
    model.addAttribute("sort", sort);
    model.addAttribute("dir", dir);
    model.addAttribute("reverseDir", dir.equals("asc") ? "desc" : "asc");
    return "view/admin/banners";
  }

  @GetMapping("/create")
  public String createBanner(@ModelAttribute("banner") BannerDto bannerDto, Model model) {
    model.addAttribute("mode", "Create");
    model.addAttribute("adminDto", userService.getCurrentUserDto());
    return "view/admin/banner";
  }

  @GetMapping("/update/{id}")
  public String updateBanner(@PathVariable long id, Model model) {
    model.addAttribute("mode", "Update");
    model.addAttribute("adminDto", userService.getCurrentUserDto());
    model.addAttribute("banner", bannerService.getBannerDtoById(id));
    return "view/admin/banner";
  }

  @PostMapping("/register")
  public String registerBanner(
      @ModelAttribute("banner") BannerDto bannerDto,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    String code;
    if (bannerDto.getId() != null) {
      model.addAttribute("mode", "Update");
      code = "success.updated";
    } else {
      model.addAttribute("mode", "Create");
      code = "success.created";
    }

    if (bindingResult.hasErrors()) {
      model.addAttribute("adminDto", userService.getCurrentUserDto());
      model.addAttribute("banner", bannerDto);
      return "view/admin/banner";
    }

    try {
      if (bannerDto.getId() != null) {
        Banner banner = bannerService.getBannerById(bannerDto.getId());
        bannerService.registerBanner(banner, bannerDto);
      } else {
        bannerService.registerBanner(new Banner(), bannerDto);
      }
    } catch (CustomException e) {
      model.addAttribute("adminDto", userService.getCurrentUserDto());
      model.addAttribute("banner", bannerDto);
      model.addAttribute("errorMessage", e.getMessage());
      return "view/admin/banner";
    }

    redirectAttributes.addFlashAttribute(
        "successMessage", messageUtil.getMessage(code, new String[] {"banner"}));
    return "redirect:/admin/banners/create";
  }
}
