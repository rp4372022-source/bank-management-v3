package com.bank.bms.transaction;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequestDto {
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private BigDecimal amount;
    private TransactionType transactionType;
}
