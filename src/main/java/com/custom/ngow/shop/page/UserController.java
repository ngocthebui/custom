package com.custom.ngow.shop.page;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.custom.ngow.shop.common.MessageUtil;
import com.custom.ngow.shop.dto.UserDto;
import com.custom.ngow.shop.dto.UserPasswordRequest;
import com.custom.ngow.shop.dto.UserRegistration;
import com.custom.ngow.shop.dto.UserResetPasswordRequest;
import com.custom.ngow.shop.entity.User;
import com.custom.ngow.shop.exception.CustomException;
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
  private final MessageUtil messageUtil;

  @PostMapping("/register")
  public String processRegister(
      @Valid @ModelAttribute("userRegistration") UserRegistration userRegistration,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      addDefaultToModel(model);
      return "view/pages/register";
    }

    try {
      userService.registerUser(userRegistration);
      redirectAttributes.addFlashAttribute(
          "successMessage", messageUtil.getMessage("success.user.register"));
      return "redirect:/login";
    } catch (CustomException e) {
      bindingResult.rejectValue(e.getField(), e.getErrorCode(), e.getArgs(), "");
      addDefaultToModel(model);
      return "view/pages/register";
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
    if (bindingResult.hasErrors()) {
      model.addAttribute("userDto", userDto);
      model.addAttribute("userPasswordRequest", new UserPasswordRequest());
      addDefaultToModel(model);
      return "view/pages/account-setting";
    }

    try {
      userService.updateUserInfo(userDto);
    } catch (CustomException e) {
      bindingResult.rejectValue(e.getField(), e.getErrorCode(), e.getArgs(), "");

      model.addAttribute("userDto", userDto);
      model.addAttribute("userPasswordRequest", new UserPasswordRequest());
      addDefaultToModel(model);
      return "view/pages/account-setting";
    }

    redirectAttributes.addFlashAttribute(
        "successMessage", messageUtil.getMessage("success.user.changeInfo"));
    return "redirect:/user/setting";
  }

  @PostMapping("/change-password")
  public String changePassword(
      @Valid @ModelAttribute("userPasswordRequest") UserPasswordRequest userPasswordRequest,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("userDto", userService.getCurrentUserDto());
      model.addAttribute("userPasswordRequest", userPasswordRequest);
      addDefaultToModel(model);
      return "view/pages/account-setting";
    }

    try {
      userService.changeUserPassword(userPasswordRequest);
    } catch (CustomException e) {
      bindingResult.rejectValue(e.getField(), e.getErrorCode(), e.getArgs(), "");

      model.addAttribute("userDto", userService.getCurrentUserDto());
      model.addAttribute("userPasswordRequest", userPasswordRequest);
      addDefaultToModel(model);
      return "view/pages/account-setting";
    }

    redirectAttributes.addFlashAttribute(
        "successMessage", messageUtil.getMessage("success.user.changePassword"));
    return "redirect:/user/setting";
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
      @Valid @ModelAttribute("resetPasswordRequest") UserResetPasswordRequest resetPasswordRequest,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      addDefaultToModel(model);
      return "view/pages/forgot-password";
    }

    try {
      userService.sendMailResetPassword(resetPasswordRequest);
    } catch (CustomException e) {
      bindingResult.rejectValue(e.getField(), e.getErrorCode(), e.getArgs(), "");

      addDefaultToModel(model);
      return "view/pages/forgot-password";
    }

    redirectAttributes.addFlashAttribute(
        "successMessage", messageUtil.getMessage("success.user.sendMailResetPassword"));
    return "redirect:/user/forgot-password";
  }

  @GetMapping("/reset-password")
  public String resetPassword(
      @RequestParam("email") String email,
      @RequestParam("otp") String otp,
      RedirectAttributes redirectAttributes) {
    try {
      userService.resetPassword(email, otp);
    } catch (CustomException e) {
      redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
      return "redirect:/login";
    }

    redirectAttributes.addFlashAttribute(
        "successMessage", messageUtil.getMessage("success.user.resetPassword"));
    return "redirect:/login";
  }

  @PostMapping("/upload-image")
  @ResponseBody
  public ResponseEntity<?> uploadAvatar(
      @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
    User user = userService.getCurrentUser();
    try {
      userService.uploadAvatar(user, file);
    } catch (RuntimeException e) {
      log.error("Validation error when uploading a photo to user: ", e);
      redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
    }
    return ResponseEntity.ok().build();
  }
}
