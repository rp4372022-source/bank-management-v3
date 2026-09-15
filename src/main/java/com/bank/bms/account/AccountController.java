package com.bank.bms.account;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountRequestDto request){
        Account saved = accountService.createAccount(request.getUserId(), request.getAccountType(), request.getInitialDeposit());

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
