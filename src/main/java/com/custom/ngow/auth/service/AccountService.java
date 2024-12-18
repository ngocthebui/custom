package com.custom.ngow.auth.service;

import com.custom.ngow.auth.dto.request.AccountRequest;
import com.custom.ngow.auth.dto.response.AccountResponse;
import com.custom.ngow.auth.enity.Account;
import com.custom.ngow.auth.repository.AccountRepository;
import com.custom.ngow.common.constant.ErrorCode;
import com.custom.ngow.common.exception.ForwardException;
import java.sql.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;
  private final ModelMapper modelMapper;
  private final PasswordEncoder passwordEncoder;

  public AccountResponse createNewAccount(AccountRequest accountRequest) {
    if (accountRepository.findByUsername(accountRequest.getUsername()).isPresent()) {
      throw new ForwardException(ErrorCode.E404101);
    }

    Account account = Account.builder()
        .email(accountRequest.getEmail())
        .username(accountRequest.getUsername())
        .password(passwordEncoder.encode(accountRequest.getPassword()))
        .createdAt(new Timestamp(System.currentTimeMillis()))
        .build();

    accountRepository.save(account);
    log.info("Create new account: {}", account.getUsername());

    return modelMapper.map(account, AccountResponse.class);
  }

  public AccountResponse getByAid(Long aid) {
    Account account = getAccountWithAid(aid);

    return modelMapper.map(account, AccountResponse.class);
  }

  public AccountResponse deleteByAid(Long aid) {
    Account account = getAccountWithAid(aid);
    accountRepository.deleteById(aid);
    log.info("Delete account: {}", account.getUsername());

    return modelMapper.map(account, AccountResponse.class);
  }

  private Account getAccountWithAid(Long aid) {
    return accountRepository.findById(aid)
        .orElseThrow(() -> new ForwardException(ErrorCode.E404101));
  }

}
