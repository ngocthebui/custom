package com.custom.ngow.shop.service;

import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.custom.ngow.shop.common.MessageUtil;
import com.custom.ngow.shop.common.SecurePasswordGenerator;
import com.custom.ngow.shop.constant.UserRole;
import com.custom.ngow.shop.dto.UserDto;
import com.custom.ngow.shop.dto.UserPasswordRequest;
import com.custom.ngow.shop.dto.UserRegistration;
import com.custom.ngow.shop.dto.UserResetPasswordRequest;
import com.custom.ngow.shop.entity.User;
import com.custom.ngow.shop.exception.CustomException;
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
  private final MessageUtil messageUtil;

  @Value("${homepage.url}")
  private String homePageUrl;

  public void registerUser(UserRegistration userRegistration) {
    log.info("Registering user {}", userRegistration.getEmail());
    if (userRepository.existsByEmail(userRegistration.getEmail())) {
      log.error("User with email {} already exists", userRegistration.getEmail());
      throw new CustomException(messageUtil, "email", new String[] {"Email"}, "error.exist");
    }

    if (!userRegistration.isPasswordMatching()) {
      log.error("Password check failed for user {}", userRegistration.getEmail());
      throw new CustomException(
          messageUtil,
          "confirmPassword",
          new String[] {"confirmPassword"},
          "error.confirmPassword");
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
    String email = userDto.getEmail();
    log.info("Updating user {}", email);

    if (!StringUtils.equals(email, user.getEmail()) && existsByEmail(email)) {
      log.error("User: {} can not update email: {}", user.getEmail(), email);
      // set correct email
      userDto.setEmail(user.getEmail());
      throw new CustomException(messageUtil, "email", new String[] {email}, "error.exist");
    }

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
    log.info("User: {} is changing password", user.getEmail());
    if (!isPasswordMatching(userPasswordRequest.getCurrentPassword(), user.getPassword())) {
      log.error("User:{} Password is incorrect", user.getEmail());
      throw new CustomException(
          messageUtil, "currentPassword", new String[] {"Password"}, "error.inCorrect");
    }

    if (!userPasswordRequest.isPasswordMatching()) {
      log.error("User:{} Password mismatch", user.getEmail());
      throw new CustomException(messageUtil, "confirmPassword", "error.confirmPassword");
    }

    user.setPassword(passwordEncoder.encode(userPasswordRequest.getPassword()));
    userRepository.save(user);

    log.info("User {} changed password successfully", user.getEmail());
  }

  public void sendMailResetPassword(UserResetPasswordRequest resetPasswordRequest) {
    String email = resetPasswordRequest.getEmail();
    log.info("User: {} is requesting reset password", email);
    if (!existsByEmail(email)) {
      log.error("User:{} email does not exist", email);
      throw new CustomException(messageUtil, "email", new String[] {email}, "error.notExist");
    }

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
    log.info("User: {} is starting reset password", email);
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(
                () ->
                    new CustomException(
                        messageUtil, "email", new String[] {"Email"}, "error.notExist"));
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
    log.info("User: {} is uploading avatar", user.getEmail());
    // create folder for user: users/avatars/{userId}/
    String folderPath = "users/avatars/" + user.getId() + "/";

    // save image
    String filename = mediaStorageService.storeImage(file, folderPath);

    // Get the full URL of the image
    String imageUrl = mediaStorageService.getFileUrl(folderPath, filename);

    // delete old image
    if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
      mediaStorageService.deleteFileByUrl(user.getImageUrl());
      log.info("User: {} is deleting old photos", user.getEmail());
    }

    user.setImageUrl(imageUrl);
    userRepository.save(user);

    log.info("User: {} updated successful avatar: {}", user.getEmail(), filename);
  }
}
