package com.custom.ngow.auth.enity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long aid;

  @NotNull
  @Size(max = 255)
  String email;

  @NotNull
  @Size(max = 255)
  String username;

  @NotNull
  @Size(max = 255)
  String password;

  @NotNull
  String role;

  @NotNull
  String status;

  @NotNull
  Timestamp createdAt;

  Timestamp updatedAt;

}
