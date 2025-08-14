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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.custom.ngow.shop.dto.UserDto;
import com.custom.ngow.shop.dto.UserPasswordRequest;
import com.custom.ngow.shop.dto.UserRegistration;
import com.custom.ngow.shop.dto.UserResetPasswordRequest;
import com.custom.ngow.shop.entity.User;
import com.custom.ngow.shop.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController extends BaseController {

  private final UserService userService;

  @PostMapping("/register")
  public String processRegister(
      @Valid @ModelAttribute("userRegistration") UserRegistration userRegistration,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    validateRegisterUser(userRegistration, bindingResult);
    if (bindingResult.hasErrors()) {
      addDefaultToModel(model);
      return "view/pages/register";
    }

    try {
      userService.registerUser(userRegistration);
      redirectAttributes.addFlashAttribute("successMessage", "success.register");
      return "redirect:/login";
    } catch (Exception e) {
      model.addAttribute("errorMessage", e.getMessage());
      addDefaultToModel(model);
      return "view/pages/register";
    }
  }

  private void validateRegisterUser(
      UserRegistration userRegistration, BindingResult bindingResult) {
    if (!userRegistration.isPasswordMatching()) {
      bindingResult.rejectValue("confirmPassword", "error.confirmPassword");
    }

    if (userService.existsByEmail(userRegistration.getEmail())) {
      bindingResult.rejectValue("email", "error.exist", new String[] {"Email"}, "");
    }
  }

  @GetMapping("/setting")
  public String setting(Model model) {
    model.addAttribute("userDto", userService.getCurrentUserDto());
    model.addAttribute("userPasswordRequest", new UserPasswordRequest());
    addDefaultToModel(model);
    return "view/pages/account-setting";
  }

  @PostMapping("/update-info")
  public String updateUser(
      @Valid @ModelAttribute("userDto") UserDto userDto,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    validateEmail(userDto, bindingResult);
    if (bindingResult.hasErrors()) {
      model.addAttribute("userDto", userDto);
      model.addAttribute("userPasswordRequest", new UserPasswordRequest());
      addDefaultToModel(model);
      return "view/pages/account-setting";
    }

    userService.updateUserInfo(userDto);

    redirectAttributes.addFlashAttribute("successMessage", "success.changeInfo");
    return "redirect:/user/setting";
  }

  private void validateEmail(UserDto userDto, BindingResult bindingResult) {
    User user = userService.getCurrentUser();
    if (!StringUtils.equals(userDto.getEmail(), user.getEmail())
        && userService.existsByEmail(userDto.getEmail())) {
      bindingResult.rejectValue("email", "error.exist", new String[] {"Email"}, "");
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
      UserDto userDto = userService.getCurrentUserDto();
      model.addAttribute("userDto", userDto);
      model.addAttribute("userPasswordRequest", userPasswordRequest);
      addDefaultToModel(model);
      return "view/pages/account-setting";
    }

    userService.changeUserPassword(userPasswordRequest);
    redirectAttributes.addFlashAttribute("successMessage", "success.changePassword");
    return "redirect:/user/setting";
  }

  private void validationUpdateUser(
      UserPasswordRequest userPasswordRequest, BindingResult bindingResult) {
    User user = userService.getCurrentUser();
    if (!userService.isPasswordMatching(
        userPasswordRequest.getCurrentPassword(), user.getPassword())) {
      bindingResult.rejectValue(
          "currentPassword", "error.inCorrect", new String[] {"Password"}, "");
      return;
    }

    if (!userPasswordRequest.isPasswordMatching()) {
      bindingResult.rejectValue("confirmPassword", "error.confirmPassword");
    }
  }

  @GetMapping("/forgot-password")
  public String forgotPassword(
      @ModelAttribute("resetPasswordRequest") UserResetPasswordRequest resetPasswordRequest,
      Model model) {
    addDefaultToModel(model);
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
      addDefaultToModel(model);
      return "view/pages/forgot-password";
    }

    userService.sendMailResetPassword(resetPasswordRequest);

    redirectAttributes.addFlashAttribute("successMessage", "success.sendMailResetPassword");
    return "redirect:/user/forgot-password";
  }

  private void validateEmailResetPassword(
      UserResetPasswordRequest userPasswordRequest, BindingResult bindingResult) {
    if (!userService.existsByEmail(userPasswordRequest.getEmail())) {
      bindingResult.rejectValue("email", "error.notExist", new String[] {"Email"}, "");
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

    redirectAttributes.addFlashAttribute("successMessage", "success.resetPassword");
    return "redirect:/login";
  }

  @PostMapping("/upload-image")
  public String uploadAvatar(
      @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
    User user = userService.getCurrentUser();
    try {
      userService.uploadAvatar(user, file);
    } catch (RuntimeException e) {
      log.error("Lỗi validation khi upload ảnh đại diện cho user: ", e);
      redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

    } catch (Exception e) {
      // Xử lý các lỗi khác
      log.error("Lỗi không xác định khi upload ảnh đại diện: ", e);
      redirectAttributes.addFlashAttribute(
          "errorMessage", "Có lỗi xảy ra khi tải lên ảnh. Vui lòng thử lại!");
    }
    redirectAttributes.addFlashAttribute("successMessage", "ok111111");
    return "redirect:/user/setting";
  }
}
