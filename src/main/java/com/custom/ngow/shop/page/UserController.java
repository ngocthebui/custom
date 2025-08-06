package com.custom.ngow.shop.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.custom.ngow.shop.dto.UserDto;
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
      @Valid @ModelAttribute("userRegistration") UserDto userDto,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    validateUser(userDto, bindingResult);
    if (bindingResult.hasErrors()) {
      addHeaderDataToModel(model);
      return "view/pages/register";
    }

    try {
      userService.registerUser(userDto);
      redirectAttributes.addFlashAttribute(
          "successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
      return "redirect:/login";
    } catch (Exception e) {
      model.addAttribute("errorMessage", e.getMessage());
      addHeaderDataToModel(model);
      return "view/pages/register";
    }
  }

  private void validateUser(UserDto userRegistration, BindingResult bindingResult) {
    if (!userRegistration.isPasswordMatching()) {
      bindingResult.rejectValue("confirmPassword", "", "Mật khẩu xác nhận không khớp");
    }

    if (userService.existsByEmail(userRegistration.getEmail())) {
      bindingResult.rejectValue("email", "", "Email đã tồn tại");
    }
  }

  @GetMapping("/setting")
  public String setting(Model model) {
    UserDto userUpdateDto = userService.getCurrentUserForUpdate();

    model.addAttribute("userUpdateDto", userUpdateDto);
    addHeaderDataToModel(model);
    return "view/pages/account-setting";
  }

  @PostMapping("/update")
  public String updateUser(
      @ModelAttribute("userUpdateDto") UserDto userUpdateDto,
      BindingResult bindingResult,
      Model model) {

    addHeaderDataToModel(model);
    return "view/pages/account-setting";
  }
}
