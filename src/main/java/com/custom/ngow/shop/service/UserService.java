package com.custom.ngow.shop.service;

import java.util.concurrent.CompletableFuture;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.custom.ngow.shop.common.SecurePasswordGenerator;
import com.custom.ngow.shop.constant.UserRole;
import com.custom.ngow.shop.dto.UserDto;
import com.custom.ngow.shop.dto.UserPasswordRequest;
import com.custom.ngow.shop.dto.UserRegistration;
import com.custom.ngow.shop.dto.UserResetPasswordRequest;
import com.custom.ngow.shop.entity.User;
import com.custom.ngow.shop.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

  private static final int TIME_OTP_EXPIRED = 5;

  private final UserRepository userRepository;
  private final ModelMapper modelMapper;
  private final PasswordEncoder passwordEncoder;
  private final MailService mailService;
  private final OtpService otpService;
  private final MediaStorageService mediaStorageService;

  @Value("${homepage.url}")
  private String homePageUrl;

  public void registerUser(UserRegistration userRegistration) {
    if (userRepository.existsByEmail(userRegistration.getEmail())) {
      throw new RuntimeException("Email is existing");
    }

    if (!userRegistration.isPasswordMatching()) {
      throw new RuntimeException("Password is not matching");
    }

    User user = new User();
    user.setFirstName(userRegistration.getFirstName());
    user.setLastName(userRegistration.getLastName());
    user.setEmail(userRegistration.getEmail());
    user.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
    user.setRole(UserRole.USER);

    userRepository.save(user);

    log.info("User {} registered successfully", userRegistration.getEmail());
  }

  public User findByEmail(String email) {
    return userRepository.findByEmail(email).orElse(null);
  }

  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  public User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null
        || !authentication.isAuthenticated()
        || authentication instanceof AnonymousAuthenticationToken) {
      return null;
    }

    String email = authentication.getName();
    return userRepository
        .findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
  }

  public UserDto getCurrentUserDto() {
    User user = getCurrentUser();
    return modelMapper.map(user, UserDto.class);
  }

  public void updateUserInfo(UserDto userDto) {
    User user = getCurrentUser();
    user.setFirstName(userDto.getFirstName());
    user.setLastName(userDto.getLastName());
    user.setEmail(userDto.getEmail());

    userRepository.save(user);

    log.info("User {} updated successfully", user.getEmail());
  }

  public boolean isPasswordMatching(String oldPassword, String newPassword) {
    return passwordEncoder.matches(oldPassword, newPassword);
  }

  public void changeUserPassword(UserPasswordRequest userPasswordRequest) {
    User user = getCurrentUser();
    if (!isPasswordMatching(userPasswordRequest.getCurrentPassword(), user.getPassword())) {
      throw new RuntimeException("Password is not matching");
    }

    if (!userPasswordRequest.isPasswordMatching()) {
      throw new RuntimeException("Confirm password is not matching");
    }

    user.setPassword(passwordEncoder.encode(userPasswordRequest.getPassword()));
    userRepository.save(user);

    log.info("User {} changed password successfully", user.getEmail());
  }

  public void sendMailResetPassword(UserResetPasswordRequest resetPasswordRequest) {
    String email = resetPasswordRequest.getEmail();
    if (existsByEmail(email)) {
      CompletableFuture.runAsync(
          () -> {
            try {
              String username = extractNameFromEmail(email);

              // otp
              String otp = otpService.createOTPWithEmail(email, TIME_OTP_EXPIRED);

              // reset link
              String linkResetPassword =
                  homePageUrl + "/user/reset-password" + "?email=" + email + "&otp=" + otp;

              // email template
              String template =
                  mailService.getTemplateByClassPathResource("templates/email/reset_password.html");
              template = template.replace("{{user_name}}", username);
              template = template.replace("{{reset_link}}", linkResetPassword);
              template = template.replace("{{time}}", String.valueOf(TIME_OTP_EXPIRED));

              // send mail
              String subject = "Reset Password";
              mailService.sendMail(email, subject, template);
            } catch (Exception e) {
              log.error("Failed to send email to: {}", email, e);
            }
          });
    }
  }

  private String extractNameFromEmail(String email) {
    if (email != null && email.contains("@")) {
      return email.split("@")[0];
    }
    return email;
  }

  public void resetPassword(String email, String otp) {
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    otpService.validOTP(email, otp);

    String password = SecurePasswordGenerator.generateStrongPassword();
    user.setPassword(passwordEncoder.encode(password));
    userRepository.save(user);
    log.info("User {} reset password successfully", email);

    // send mail
    CompletableFuture.runAsync(
        () -> {
          try {
            String username = extractNameFromEmail(email);

            // email template
            String template =
                mailService.getTemplateByClassPathResource("templates/email/new_password.html");
            template = template.replace("{{user_name}}", username);
            template = template.replace("{{new_password}}", password);

            // send mail
            String subject = "New Password";
            mailService.sendMail(email, subject, template);
          } catch (Exception e) {
            log.error("Failed to send email to: {}", email, e);
          }
        });
  }

  public void uploadAvatar(User user, MultipartFile file) {
    // create folder for user: users/avatars/{userId}/
    String folderPath = "users/avatars/" + user.getId() + "/";

    // save image
    String filename = mediaStorageService.storeImage(file, folderPath);

    // Get the full URL of the image
    String imageUrl = mediaStorageService.getFileUrl(folderPath, filename);

    // delete old image
    if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
      mediaStorageService.deleteFileByUrl(user.getImageUrl());
      log.info("User {} replacing old photos", user.getId());
    }

    user.setImageUrl(imageUrl);
    userRepository.save(user);

    log.info("User {} updated successful avatar: {}", user.getId(), filename);
  }
}
