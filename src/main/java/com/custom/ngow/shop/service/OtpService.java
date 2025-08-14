package com.custom.ngow.shop.service;

import java.security.SecureRandom;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.custom.ngow.shop.common.MessageUtil;
import com.custom.ngow.shop.entity.Otp;
import com.custom.ngow.shop.exception.CustomException;
import com.custom.ngow.shop.repository.OtpRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OtpService {

  // 5 minutes
  private static final long MINUTES = 60 * 1000;
  private static final int OTP_LENGTH = 6;

  private final OtpRepository otpRepository;
  private final MessageUtil messageUtil;

  public String createOTPWithEmail(String email, int time) {
    Otp otp =
        Otp.builder()
            .email(email)
            .otp(generateOtp())
            .expiredTime(System.currentTimeMillis() + time * MINUTES)
            .isActive(true)
            .build();

    otpRepository.save(otp);
    log.info("{} Otp has been created", email);

    return otp.getOtp();
  }

  public void validOTP(String email, String otp) {
    Otp oldOTP = getOTPByEmail(email);

    if (!StringUtils.equals(otp, oldOTP.getOtp())) {
      throw new CustomException(messageUtil, "", new String[] {"OTP"}, "error.inCorrect");
    }

    if (System.currentTimeMillis() > oldOTP.getExpiredTime()) {
      throw new CustomException(messageUtil, "", new String[] {"OTP"}, "error.expired");
    }

    if (!oldOTP.isActive()) {
      throw new CustomException(messageUtil, "", new String[] {"OTP"}, "error.inCorrect");
    }

    oldOTP.setActive(false);
    otpRepository.save(oldOTP);
  }

  public Otp getOTPByEmail(String email) {
    return otpRepository
        .findByEmail(email)
        .orElseThrow(
            () -> new CustomException(messageUtil, "", new String[] {"OTP"}, "error.notFound"));
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
