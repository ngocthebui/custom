package com.custom.ngow.shop.page.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.custom.ngow.shop.common.MessageUtil;
import com.custom.ngow.shop.constant.UserRole;
import com.custom.ngow.shop.constant.UserStatus;
import com.custom.ngow.shop.dto.UserDto;
import com.custom.ngow.shop.exception.CustomException;
import com.custom.ngow.shop.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller("Admin-UserController")
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

  private static final int PAGE_SIZE = 5;

  private final UserService userService;
  private final MessageUtil messageUtil;

  @GetMapping
  public String getUsers(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "id") String sort,
      @RequestParam(defaultValue = "asc") String dir,
      Model model) {
    model.addAttribute("countActiveUsers", userService.countUsersByStatus(UserStatus.ACTIVE));
    model.addAttribute("countInactiveUsers", userService.countUsersByStatus(UserStatus.INACTIVE));
    model.addAttribute("countTotalUsers", userService.countAllUser());

    model.addAttribute("userPage", userService.getUsers(page, PAGE_SIZE, sort, dir));
    model.addAttribute("adminDto", userService.getCurrentUserDto());

    model.addAttribute("pagingUrl", "/admin/users");
    model.addAttribute("sort", sort);
    model.addAttribute("dir", dir);
    model.addAttribute("reverseDir", dir.equals("asc") ? "desc" : "asc");
    return "view/admin/users";
  }

  @GetMapping("/search")
  public String getUsersByEmail(
      @RequestParam String q,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "id") String sort,
      @RequestParam(defaultValue = "asc") String dir,
      Model model) {
    model.addAttribute("countActiveUsers", userService.countUsersByStatus(UserStatus.ACTIVE));
    model.addAttribute("countInactiveUsers", userService.countUsersByStatus(UserStatus.INACTIVE));
    model.addAttribute("countTotalUsers", userService.countAllUser());

    model.addAttribute("userPage", userService.searchByEmailContain(q, page, PAGE_SIZE, sort, dir));
    model.addAttribute("adminDto", userService.getCurrentUserDto());

    model.addAttribute("pagingUrl", "/admin/users/search?q=" + q);
    model.addAttribute("sort", sort);
    model.addAttribute("dir", dir);
    model.addAttribute("reverseDir", dir.equals("asc") ? "desc" : "asc");
    return "view/admin/users";
  }

  @GetMapping("/update/{id}")
  public String updateUser(@PathVariable("id") long id, Model model) {
    model.addAttribute("adminDto", userService.getCurrentUserDto());
    model.addAttribute("userDto", userService.getUserDtoById(id));

    model.addAttribute("roleList", UserRole.values());
    model.addAttribute("statusList", UserStatus.values());
    return "view/admin/user-update";
  }

  @PostMapping("/update")
  public String updateUser(@ModelAttribute UserDto userDto, RedirectAttributes redirectAttributes) {
    try {
      userService.updateRoleAndStatus(userDto);
    } catch (CustomException e) {
      redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
      return "redirect:/admin/users/update/" + userDto.getId();
    }

    redirectAttributes.addFlashAttribute(
        "successMessage", messageUtil.getMessage("success.user.changeInfo"));
    return "redirect:/admin/users/update/" + userDto.getId();
  }
}
