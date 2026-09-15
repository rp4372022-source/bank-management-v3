package com.bank.bms.account;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRequestDto {
    private Long userId;
    private AccountType accountType;
    private BigDecimal initialDeposit;
}
