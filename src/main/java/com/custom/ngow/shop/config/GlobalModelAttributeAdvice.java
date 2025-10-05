package com.custom.ngow.shop.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.custom.ngow.shop.constant.UserRole;
import com.custom.ngow.shop.entity.User;
import com.custom.ngow.shop.service.UserAuthenticationService;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributeAdvice {

  private final UserAuthenticationService userAuthenticationService;

  @ModelAttribute("isAdmin")
  public Boolean isAdmin() {
    if (isUserLoggedIn()) {
      User user = userAuthenticationService.getCurrentUser();
      return user.getRole() == UserRole.ADMIN;
    }
    return false;
  }

  private boolean isUserLoggedIn() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null
        && authentication.isAuthenticated()
        && !authentication.getName().equals("anonymousUser");
  }
}
