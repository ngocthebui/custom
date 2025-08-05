package com.custom.ngow.shop.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.custom.ngow.shop.constant.UserRole;
import com.custom.ngow.shop.dto.UserRegistrationDto;
import com.custom.ngow.shop.entity.User;
import com.custom.ngow.shop.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public User registerUser(UserRegistrationDto userRegistration) {
    if (userRepository.existsByEmail(userRegistration.getEmail())) {
      throw new RuntimeException("Email is existing");
    }

    if (!userRegistration.isPasswordMatching()) {
      throw new RuntimeException("Password is not matching");
    }

    User user = new User();
    user.setEmail(userRegistration.getEmail());
    user.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
    user.setRole(UserRole.USER);

    return userRepository.save(user);
  }

  public User findByEmail(String email) {
    return userRepository.findByEmail(email).orElse(null);
  }

  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }
}
