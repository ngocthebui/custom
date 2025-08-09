package com.custom.ngow.shop.page;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.custom.ngow.shop.dto.UserDto;
import com.custom.ngow.shop.dto.UserInfoRequest;
import com.custom.ngow.shop.dto.UserPasswordRequest;
import com.custom.ngow.shop.dto.UserResetPasswordRequest;
import com.custom.ngow.shop.entity.User;
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
    validateRegisterUser(userDto, bindingResult);
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

  private void validateRegisterUser(UserDto userRegistration, BindingResult bindingResult) {
    if (!userRegistration.isPasswordMatching()) {
      bindingResult.rejectValue("confirmPassword", "", "Mật khẩu xác nhận không khớp");
    }

    if (userService.existsByEmail(userRegistration.getEmail())) {
      bindingResult.rejectValue("email", "", "Email đã tồn tại");
    }
  }

  @GetMapping("/setting")
  public String setting(Model model) {
    UserInfoRequest userInfoRequest = userService.getCurrentUserForUpdate();

    model.addAttribute("userInfoRequest", userInfoRequest);
    model.addAttribute("userPasswordRequest", new UserPasswordRequest());
    addHeaderDataToModel(model);
    return "view/pages/account-setting";
  }

  @PostMapping("/update-info")
  public String updateUser(
      @Valid @ModelAttribute("userInfoRequest") UserInfoRequest userInfoRequest,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    validateEmail(userInfoRequest, bindingResult);
    if (bindingResult.hasErrors()) {
      model.addAttribute("userInfoRequest", userInfoRequest);
      model.addAttribute("userPasswordRequest", new UserPasswordRequest());
      addHeaderDataToModel(model);
      return "view/pages/account-setting";
    }

    userService.updateUserInfo(userInfoRequest);

    redirectAttributes.addFlashAttribute("successMessage", "Thay đổi thông tin thành công");
    return "redirect:/user/setting";
  }

  private void validateEmail(UserInfoRequest userInfoRequest, BindingResult bindingResult) {
    User user = userService.getCurrentUser();
    if (!StringUtils.equals(userInfoRequest.getEmail(), user.getEmail())
        && userService.existsByEmail(userInfoRequest.getEmail())) {
      bindingResult.rejectValue("email", "", "Email đã tồn tại");
    }
  }

  @PostMapping("/change-password")
  public String changePassword(
      @Valid @ModelAttribute("userPasswordRequest") UserPasswordRequest userPasswordRequest,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    validationUpdateUser(userPasswordRequest, bindingResult);
    if (bindingResult.hasErrors()) {
      UserInfoRequest userInfoRequest = userService.getCurrentUserForUpdate();
      model.addAttribute("userInfoRequest", userInfoRequest);
      model.addAttribute("userPasswordRequest", userPasswordRequest);
      addHeaderDataToModel(model);
      return "view/pages/account-setting";
    }

    userService.changeUserPassword(userPasswordRequest);
    redirectAttributes.addFlashAttribute("successMessage", "Thay đổi mật khẩu thành công");
    return "redirect:/user/setting";
  }

  private void validationUpdateUser(
      UserPasswordRequest userPasswordRequest, BindingResult bindingResult) {
    User user = userService.getCurrentUser();
    if (!userService.isPasswordMatching(
        userPasswordRequest.getCurrentPassword(), user.getPassword())) {
      bindingResult.rejectValue("currentPassword", "", "Mật khẩu không chính xác");
      return;
    }

    if (!userPasswordRequest.isPasswordMatching()) {
      bindingResult.rejectValue("confirmPassword", "", "Mật khẩu xác nhận không khớp");
    }
  }

  @GetMapping("/forgot-password")
  public String forgotPassword(
      @ModelAttribute("resetPasswordRequest") UserResetPasswordRequest resetPasswordRequest,
      Model model) {
    addHeaderDataToModel(model);
    return "view/pages/forgot-password";
  }

  @PostMapping("/reset-password")
  public String resetPassword(
      @ModelAttribute("resetPasswordRequest") UserResetPasswordRequest resetPasswordRequest,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    validateEmailResetPassword(resetPasswordRequest, bindingResult);
    if (bindingResult.hasErrors()) {
      addHeaderDataToModel(model);
      return "view/pages/forgot-password";
    }

    userService.sendMailResetPassword(resetPasswordRequest);

    redirectAttributes.addFlashAttribute(
        "successMessage",
        "Email sent successfully! Please check your inbox and spam folder. If you don't receive the email within 5 minutes, try again.");
    return "redirect:/user/forgot-password";
  }

  private void validateEmailResetPassword(
      UserResetPasswordRequest userPasswordRequest, BindingResult bindingResult) {
    if (!userService.existsByEmail(userPasswordRequest.getEmail())) {
      bindingResult.rejectValue("email", "", "Email chưa đăng ký");
    }
  }

  @GetMapping("/reset-password")
  public String resetPassword(
      @RequestParam("email") String email,
      @RequestParam("otp") String otp,
      RedirectAttributes redirectAttributes) {
    try {
      userService.resetPassword(email, otp);
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
      return "redirect:/login";
    }

    redirectAttributes.addFlashAttribute("successMessage", "Reset Password Successfully");
    return "redirect:/login";
  }
}
