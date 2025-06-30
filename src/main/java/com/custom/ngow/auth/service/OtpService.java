package com.custom.ngow.auth.service;

import com.custom.ngow.auth.enity.Otp;
import com.custom.ngow.auth.repository.OtpRepository;
import com.custom.ngow.common.constant.ErrorCode;
import com.custom.ngow.common.exception.ForwardException;
import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpService {

  private static final long OTP_EXPIRED_TIME = 5 * 60 * 1000;
  private static final int OTP_LENGTH = 6;

  private final OtpRepository otpRepository;

  public String createOTPWithEmail(String email) {
    Otp otp = Otp.builder()
        .email(email)
        .otp(generateOtp())
        .expiredTime(System.currentTimeMillis() + OTP_EXPIRED_TIME)
        .isActive(true)
        .build();

    otpRepository.save(otp);

    return otp.getOtp();
  }

  public void validOTP(String email, String otp) {
    Otp oldOTP = getOTPByEmail(email);

    if (!StringUtils.equals(otp, oldOTP.getOtp())) {
      throw new ForwardException(ErrorCode.E401100, "OTP is not correct");
    }

    if (System.currentTimeMillis() > oldOTP.getExpiredTime()) {
      throw new ForwardException(ErrorCode.E401100, "OTP is expired");
    }

    if (!oldOTP.isActive()) {
      throw new ForwardException(ErrorCode.E401100, "OTP is inactive");
    }

    oldOTP.setActive(false);
    otpRepository.save(oldOTP);
  }

  public Otp getOTPByEmail(String email) {
    return otpRepository.findById(email)
        .orElseThrow(() -> new ForwardException(ErrorCode.E404100, "Can not find OTP"));
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
