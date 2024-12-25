package com.custom.ngow.auth.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;

import com.custom.ngow.auth.constant.AccountStatus;
import com.custom.ngow.auth.constant.Role;
import com.custom.ngow.auth.dto.request.AccountRequest;
import com.custom.ngow.auth.enity.Account;
import com.custom.ngow.common.constant.ErrorCode;
import com.custom.ngow.common.exception.ForwardException;
import com.custom.ngow.config.TestContainersConfig;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class AccountServiceTest extends TestContainersConfig {

    @Autowired
    private AccountService accountService;

    @MockitoBean
    private MailService mailService;

    @Test
    void createAccount_validRequest() {
        //given
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setEmail("the.bui@tda.company");
        accountRequest.setUsername("test");
        accountRequest.setPassword("test");

        //when
        Account account = accountService.createNewAccount(accountRequest);
        Mockito.doNothing().when(mailService).sendActiveAccount(anyString(), anyString());

        //then
        assertAll(
                () -> assertEquals("the.bui@tda.company", account.getEmail()),
                () -> assertEquals("test", account.getUsername()),
                () -> assertEquals(AccountStatus.WAITING.name(), account.getStatus()),
                () -> assertEquals(Role.USER.name(), account.getRole())
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
        assertEquals(ErrorCode.E400102, actualException.getErrorCode());
    }

    @Test
    void getAccountByAid_validAid() throws ParseException {
        //given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse("2024-12-20 11:44:54");

        Account expectedAccount = Account.builder()
                .aid(2L)
                .email("ngow@gmail.com")
                .username("ngow")
                .createdAt(new Timestamp(date.getTime()))
                .updatedAt(new Timestamp(date.getTime()))
                .role(Role.USER.name())
                .status(AccountStatus.ACTIVE.name())
                .build();

        //when
        Account actualAccount = accountService.getByAid(2L);

        //then
        assertAll(
                () -> assertEquals(expectedAccount.getAid(), actualAccount.getAid()),
                () -> assertEquals(expectedAccount.getEmail(), actualAccount.getEmail()),
                () -> assertEquals(expectedAccount.getUsername(), actualAccount.getUsername()),
                () -> assertEquals(expectedAccount.getCreatedAt(), actualAccount.getCreatedAt()),
                () -> assertEquals(expectedAccount.getUpdatedAt(), actualAccount.getUpdatedAt()),
                () -> assertEquals(expectedAccount.getRole(), actualAccount.getRole()),
                () -> assertEquals(expectedAccount.getStatus(), actualAccount.getStatus())
        );
    }

    @Test
    void getAccountByAid_invalidAid() {
        //when
        ForwardException exception = assertThrows(ForwardException.class,
                () -> accountService.getByAid(-1L));

        //then
        assertEquals(ErrorCode.E404101, exception.getErrorCode());
    }

    @Test
    void softDeleteAccountByAid_validAid() throws ParseException {
        //given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse("2024-12-20 11:44:54");

        Account expectedAccount = Account.builder()
                .aid(2L)
                .email("ngow@gmail.com")
                .username("ngow")
                .createdAt(new Timestamp(date.getTime()))
                .role(Role.USER.name())
                .status(AccountStatus.DELETE.name())
                .build();

        //when
        Account actualAccount = accountService.softDeleteByAid(2L);

        //then
        assertAll(
                () -> assertEquals(expectedAccount.getAid(), actualAccount.getAid()),
                () -> assertEquals(expectedAccount.getEmail(), actualAccount.getEmail()),
                () -> assertEquals(expectedAccount.getUsername(), actualAccount.getUsername()),
                () -> assertEquals(expectedAccount.getCreatedAt(), actualAccount.getCreatedAt()),
                () -> assertEquals(expectedAccount.getRole(), actualAccount.getRole()),
                () -> assertEquals(expectedAccount.getStatus(), actualAccount.getStatus())
        );
    }

}