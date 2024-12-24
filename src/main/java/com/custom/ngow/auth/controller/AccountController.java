package com.custom.ngow.auth.controller;

import com.custom.ngow.auth.dto.request.AccountRequest;
import com.custom.ngow.auth.dto.response.AccountResponse;
import com.custom.ngow.auth.service.AccountService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@Validated
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<AccountResponse> createAccount(
            @RequestBody @Valid AccountRequest accountRequest) {
        AccountResponse response = modelMapper
                .map(accountService.createNewAccount(accountRequest), AccountResponse.class);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{aid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable("aid") Long aid) {
        AccountResponse response = modelMapper.map(accountService.getByAid(aid),
                AccountResponse.class);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{aid}")
    public ResponseEntity<AccountResponse> softDeleteAccount(@PathVariable("aid") Long aid) {
        AccountResponse response = modelMapper
                .map(accountService.softDeleteByAid(aid), AccountResponse.class);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{aid}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AccountResponse> blockAccount(@PathVariable("aid") Long aid,
            @RequestParam String status) {
        AccountResponse response = modelMapper
                .map(accountService.updateStatusByAid(aid, status), AccountResponse.class);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AccountResponse>> getAll() {
        List<AccountResponse> response = accountService.getAllAccounts()
                .stream().map(account -> modelMapper.map(account, AccountResponse.class)).toList();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/myInfo")
    public ResponseEntity<AccountResponse> getMyInfo() {
        AccountResponse response = modelMapper.map(accountService.getMyInfo(),
                AccountResponse.class);
        return ResponseEntity.ok().body(response);
    }

}
