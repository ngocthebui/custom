package com.custom.ngow.auth.controller;

import com.custom.ngow.auth.dto.request.AuthenticationRequest;
import com.custom.ngow.auth.dto.response.AuthenticationResponse;
import com.custom.ngow.auth.service.AuthenticationService;
import com.custom.ngow.auth.service.MailService;
import com.custom.ngow.auth.service.OtpService;
import com.custom.ngow.common.constant.Regex;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;
  private final MailService mailService;
  private final OtpService otpService;

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request) {
    return ResponseEntity.ok(authenticationService.authenticate(request));
  }

  @PostMapping("/otp/send")
  public ResponseEntity<String> sendOTP(
      @RequestParam @Pattern(regexp = Regex.EMAIL) String email) {
    mailService.sendOtp(email);
    return ResponseEntity.ok("OTP sent to " + email);
  }

  @PostMapping("/otp/verify")
  public ResponseEntity<String> verifyOTP(
      @RequestParam @Pattern(regexp = Regex.EMAIL) String email,
      @RequestParam String otp) {
    otpService.validOTP(email, otp);
    return ResponseEntity.ok("OTP is valid");
  }

  @PostMapping("/active/{username}")
  public ResponseEntity<String> verifyAccount(
      @PathVariable("username") @Pattern(regexp = Regex.USERNAME) String username,
      @RequestParam String otp) {
    authenticationService.activeAccount(username, otp);
    return ResponseEntity.ok("Verify account: " + username);
  }
}
