package com.custom.ngow.shop.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.custom.ngow.shop.dto.RegisterRequest;
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
      @Valid @ModelAttribute RegisterRequest registerRequest,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    validateUser(registerRequest, bindingResult);
    if (bindingResult.hasErrors()) {
      addHeaderDataToModel(model);
      return "view/pages/register";
    }

    try {
      userService.registerUser(registerRequest);
      redirectAttributes.addFlashAttribute(
          "successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
      return "redirect:/login";
    } catch (Exception e) {
      model.addAttribute("registerRequest", registerRequest);
      model.addAttribute("errorMessage", e.getMessage());
      addHeaderDataToModel(model);
      return "view/pages/register";
    }
  }

  private void validateUser(RegisterRequest registerRequest, BindingResult bindingResult) {
    if (!registerRequest.isPasswordMatching()) {
      bindingResult.rejectValue("confirmPassword", "", "Mật khẩu xác nhận không khớp");
    }

    if (userService.existsByEmail(registerRequest.getEmail())) {
      bindingResult.rejectValue("email", "", "Email đã tồn tại");
    }
  }
}
