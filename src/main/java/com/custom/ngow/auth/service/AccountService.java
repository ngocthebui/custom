package com.custom.ngow.auth.service;

import com.custom.ngow.auth.constant.AccountStatus;
import com.custom.ngow.auth.constant.Role;
import com.custom.ngow.auth.dto.request.AccountRequest;
import com.custom.ngow.auth.enity.Account;
import com.custom.ngow.auth.repository.AccountRepository;
import com.custom.ngow.common.constant.ErrorCode;
import com.custom.ngow.common.exception.ForwardException;
import java.sql.Timestamp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;
  private final PasswordEncoder passwordEncoder;
  private final MailService mailService;

  public Account createNewAccount(AccountRequest accountRequest) {
    if (accountRepository.findByUsername(accountRequest.getUsername()).isPresent()) {
      throw new ForwardException(ErrorCode.E400102);
    }

    Timestamp time = new Timestamp(System.currentTimeMillis());

    Account requestAccount = Account.builder()
        .email(accountRequest.getEmail())
        .username(accountRequest.getUsername())
        .password(passwordEncoder.encode(accountRequest.getPassword()))
        .role(Role.USER.name())
        .status(AccountStatus.WAITING.name())
        .createdAt(time)
        .updatedAt(time)
        .build();

    Account account = accountRepository.save(requestAccount);
    log.info("Create new account: {}", account.getUsername());

    mailService.sendActiveAccount(account.getUsername(), account.getEmail());
    return account;
  }

  public Account softDeleteByAid(Long aid) {
    Account account = getByAid(aid);
    return updateStatus(account, AccountStatus.DELETE);
  }

  public Account updateStatusByAid(Long aid, String status) {
    Account account = getByAid(aid);
    AccountStatus accountStatus;
    try {
      accountStatus = AccountStatus.valueOf(StringUtils.upperCase(status));
    } catch (IllegalArgumentException exception) {
      throw new ForwardException(ErrorCode.E404100, "Can not find status");
    }

    return updateStatus(account, accountStatus);
  }

  public Account updateStatus(Account account, AccountStatus status) {
    account.setStatus(status.name());
    account.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

    accountRepository.save(account);
    log.info("{} account: {}", status.name(), account.getUsername());
    return account;
  }

  public Account getByAid(Long aid) {
    return accountRepository.findById(aid)
        .orElseThrow(() -> new ForwardException(ErrorCode.E404101));
  }

  public Account getByUsername(String username) {
    return accountRepository.findByUsername(username)
        .orElseThrow(() -> new ForwardException(ErrorCode.E404101));
  }

  public List<Account> getAllAccounts() {
    return accountRepository.findAll();
  }

  public Account getMyInfo() {
    var context = SecurityContextHolder.getContext();
    String username = context.getAuthentication().getName();

    return accountRepository.findByUsername(username)
        .orElseThrow(() -> new ForwardException(ErrorCode.E404101));
  }

}
