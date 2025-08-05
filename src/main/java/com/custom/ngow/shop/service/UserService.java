package com.custom.ngow.shop.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.custom.ngow.shop.constant.UserRole;
import com.custom.ngow.shop.dto.RegisterRequest;
import com.custom.ngow.shop.entity.User;
import com.custom.ngow.shop.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public User registerUser(RegisterRequest registerRequest) {
    if (userRepository.existsByEmail(registerRequest.getEmail())) {
      throw new RuntimeException("Email is existing");
    }

    if (!registerRequest.isPasswordMatching()) {
      throw new RuntimeException("Password is not matching");
    }

    User user = new User();
    user.setEmail(registerRequest.getEmail());
    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
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
