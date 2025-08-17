package com.custom.ngow.shop.page;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.custom.ngow.shop.dto.UserDto;
import com.custom.ngow.shop.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

  private final UserService userService;

  @GetMapping("/users")
  public String users(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size,
      Model model) {
    Page<UserDto> userPage = userService.getUsers(page, size);

    model.addAttribute("userPage", userPage);
    model.addAttribute("adminDto", userService.getCurrentUserDto());
    return "view/admin/users";
  }
}
