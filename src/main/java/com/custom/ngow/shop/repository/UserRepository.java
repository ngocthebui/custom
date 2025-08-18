package com.custom.ngow.shop.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.custom.ngow.shop.constant.UserStatus;
import com.custom.ngow.shop.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  Page<User> findAll(Pageable pageable);

  long countByStatus(UserStatus status);

  Page<User> findAllByEmailContainsIgnoreCase(String email, Pageable pageable);
}
