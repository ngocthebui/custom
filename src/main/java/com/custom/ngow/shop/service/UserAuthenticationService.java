package com.custom.ngow.shop.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.custom.ngow.shop.common.MessageUtil;
import com.custom.ngow.shop.entity.User;
import com.custom.ngow.shop.exception.CustomException;
import com.custom.ngow.shop.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAuthenticationService {

  private final UserRepository userRepository;
  private final MessageUtil messageUtil;

  public boolean isUserLoggedIn() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null
        && authentication.isAuthenticated()
        && !authentication.getName().equals("anonymousUser");
  }

  public String getCurrentUserEmail() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (isUserLoggedIn()) {
      return authentication.getName();
    }
    return null;
  }

  public User getCurrentUser() {
    String email = getCurrentUserEmail();
    return userRepository
        .findByEmail(email)
        .orElseThrow(
            () -> new CustomException(messageUtil, "", new String[] {"Email"}, "error.notExist"));
  }
}
