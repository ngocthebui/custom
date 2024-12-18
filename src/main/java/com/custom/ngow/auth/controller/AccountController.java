package com.custom.ngow.auth.controller;


import com.custom.ngow.auth.dto.request.AccountRequest;
import com.custom.ngow.auth.dto.response.AccountResponse;
import com.custom.ngow.auth.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  @PostMapping("/create")
  public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest accountRequest) {
    return ResponseEntity.ok().body(accountService.createNewAccount(accountRequest));
  }

  @GetMapping("/{aid}")
  public ResponseEntity<AccountResponse> getAccountByAid(@PathVariable("aid") Long aid) {
    return ResponseEntity.ok().body(accountService.getByAid(aid));
  }

  @DeleteMapping("/{aid}")
  public ResponseEntity<AccountResponse> deleteAccountByAid(@PathVariable("aid") Long aid) {
    return ResponseEntity.ok().body(accountService.deleteByAid(aid));
  }

}
