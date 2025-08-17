package com.custom.ngow.shop.config;

import java.io.IOException;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException, ServletException {
    String errorCode;

    //    if (exception instanceof LockedException) {
    //      errorCode = "account.locked";
    //    } else
    if (exception instanceof DisabledException) {
      errorCode = "error.disable";
      //    } else if (exception instanceof CredentialsExpiredException) {
      //      errorCode = "account.credentialsExpired";
      //    } else if (exception instanceof AccountExpiredException) {
      //      errorCode = "account.expired";
    } else {
      errorCode = "error.login";
    }

    // Gửi code về query string để controller xử lý message
    response.sendRedirect("/login?error=" + errorCode);
  }
}
