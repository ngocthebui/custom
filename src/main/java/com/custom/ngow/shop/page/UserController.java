package com.custom.ngow.shop.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.custom.ngow.shop.dto.UserRegistrationDto;
import com.custom.ngow.shop.dto.UserUpdateDto;
import com.custom.ngow.shop.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController extends BaseController {

  private final UserService userService;

  @PostMapping("/register")
  public String processRegister(
      @Valid @ModelAttribute("userRegistration") UserRegistrationDto userRegistration,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    validateUser(userRegistration, bindingResult);
    if (bindingResult.hasErrors()) {
      addHeaderDataToModel(model);
      return "view/pages/register";
    }

    try {
      userService.registerUser(userRegistration);
      redirectAttributes.addFlashAttribute(
          "successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
      return "redirect:/login";
    } catch (Exception e) {
      model.addAttribute("errorMessage", e.getMessage());
      addHeaderDataToModel(model);
      return "view/pages/register";
    }
  }

  private void validateUser(UserRegistrationDto userRegistration, BindingResult bindingResult) {
    if (!userRegistration.isPasswordMatching()) {
      bindingResult.rejectValue("confirmPassword", "", "Mật khẩu xác nhận không khớp");
    }

    if (userService.existsByEmail(userRegistration.getEmail())) {
      bindingResult.rejectValue("email", "", "Email đã tồn tại");
    }
  }

  @GetMapping("/setting")
  public String setting(Model model) {
    addHeaderDataToModel(model);
    return "view/pages/account-setting";
  }

  @PostMapping("/update/{id}")
  public String updateUser(
      @PathVariable("id") Long id,
      @ModelAttribute("userUpdateDto") UserUpdateDto userUpdateDto,
      BindingResult bindingResult,
      Model model) {

    addHeaderDataToModel(model);
    return "view/pages/account-setting";
  }
}
