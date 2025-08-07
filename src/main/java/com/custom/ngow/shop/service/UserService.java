package com.custom.ngow.shop.service;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.custom.ngow.shop.constant.UserRole;
import com.custom.ngow.shop.dto.UserDto;
import com.custom.ngow.shop.dto.UserInfoRequest;
import com.custom.ngow.shop.dto.UserPasswordRequest;
import com.custom.ngow.shop.entity.User;
import com.custom.ngow.shop.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void registerUser(UserDto userRegistration) {
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

  public UserInfoRequest getCurrentUserForUpdate() {
    User user = getCurrentUser();

    UserInfoRequest userDto = new UserInfoRequest();
    userDto.setEmail(user.getEmail());
    userDto.setFirstName(user.getFirstName());
    userDto.setLastName(user.getLastName());
    return userDto;
  }

  public void updateUserInfo(UserInfoRequest userDto) {
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
}
