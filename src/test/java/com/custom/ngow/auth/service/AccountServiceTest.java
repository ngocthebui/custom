package com.custom.ngow.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.custom.ngow.auth.constant.AccountStatus;
import com.custom.ngow.auth.constant.Role;
import com.custom.ngow.auth.dto.request.AccountRequest;
import com.custom.ngow.auth.dto.response.AccountResponse;
import com.custom.ngow.common.constant.ErrorCode;
import com.custom.ngow.common.exception.ForwardException;
import com.custom.ngow.config.TestContainersConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AccountServiceTest extends TestContainersConfig {

  @Autowired
  private AccountService accountService;

  @Test
  void createAccount_validRequest() {
    //given
    AccountRequest accountRequest = new AccountRequest();
    accountRequest.setEmail("test@gmail.com");
    accountRequest.setUsername("test");
    accountRequest.setPassword("test");

    //when
    AccountResponse accountResponse = accountService.createNewAccount(accountRequest);

    //then
    Assertions.assertAll(
        () -> assertEquals("test@gmail.com", accountResponse.getEmail()),
        () -> assertEquals("test", accountResponse.getUsername()),
        () -> assertEquals(AccountStatus.ACTIVE.name(), accountResponse.getStatus()),
        () -> assertEquals(Role.USER.name(), accountResponse.getRole())
    );
  }

  @Test
  void createAccount_invalidRequest() {
    //given
    AccountRequest accountRequest = new AccountRequest();
    accountRequest.setEmail("test@gmail.com");
    accountRequest.setUsername("ngow");
    accountRequest.setPassword("test");

    //when
    ForwardException actualException = assertThrows(ForwardException.class,
        () -> accountService.createNewAccount(accountRequest));

    //then
    assertEquals(ErrorCode.E404101, actualException.getErrorCode());
  }

}