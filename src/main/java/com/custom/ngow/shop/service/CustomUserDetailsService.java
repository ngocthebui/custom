package com.custom.ngow.shop.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.custom.ngow.shop.constant.UserStatus;
import com.custom.ngow.shop.entity.User;
import com.custom.ngow.shop.exception.CustomException;
import com.custom.ngow.shop.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new CustomException("User not found with email: " + email));

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getEmail())
        .password(user.getPassword())
        .authorities(new SimpleGrantedAuthority(user.getRole().name()))
        //                .accountExpired(!user.isAccountNonExpired())
        //                .accountLocked(!user.isAccountNonLocked())
        //                .credentialsExpired(!user.isCredentialsNonExpired())
        .disabled(user.getStatus() == UserStatus.INACTIVE)
        .build();
  }
}
