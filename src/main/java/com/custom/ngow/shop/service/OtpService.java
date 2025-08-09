package com.custom.ngow.shop.service;

import java.security.SecureRandom;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.custom.ngow.shop.entity.Otp;
import com.custom.ngow.shop.repository.OtpRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtpService {

  // 5 minutes
  private static final long MINUTES = 60 * 1000;
  private static final int OTP_LENGTH = 6;

  private final OtpRepository otpRepository;

  public String createOTPWithEmail(String email, int time) {
    Otp otp =
        Otp.builder()
            .email(email)
            .otp(generateOtp())
            .expiredTime(System.currentTimeMillis() + time * MINUTES)
            .isActive(true)
            .build();

    otpRepository.save(otp);

    return otp.getOtp();
  }

  public void validOTP(String email, String otp) {
    Otp oldOTP = getOTPByEmail(email);

    if (!StringUtils.equals(otp, oldOTP.getOtp())) {
      throw new RuntimeException("OTP is not correct");
    }

    if (System.currentTimeMillis() > oldOTP.getExpiredTime()) {
      throw new RuntimeException("OTP is expired");
    }

    if (!oldOTP.isActive()) {
      throw new RuntimeException("OTP is inactive");
    }

    oldOTP.setActive(false);
    otpRepository.save(oldOTP);
  }

  public Otp getOTPByEmail(String email) {
    return otpRepository
        .findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Can not find OTP"));
  }

  private String generateOtp() {
    SecureRandom random = new SecureRandom();
    StringBuilder otp = new StringBuilder();
    for (int i = 0; i < OTP_LENGTH; i++) {
      otp.append(random.nextInt(10));
    }
    return otp.toString();
  }
}
