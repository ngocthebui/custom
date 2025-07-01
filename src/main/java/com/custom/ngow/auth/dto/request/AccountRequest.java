package com.custom.ngow.auth.dto.request;

import com.custom.ngow.common.constant.Regex;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountRequest {

  @Pattern(regexp = Regex.EMAIL)
  String email;

  @Pattern(regexp = Regex.USERNAME)
  String username;

  @Pattern(regexp = Regex.PASSWORD)
  String password;

}
