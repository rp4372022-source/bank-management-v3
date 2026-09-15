package com.bank.bms.transaction;

import java.math.BigDecimal;

public interface TransactionService {
    Transaction deposit(String accountNumber, BigDecimal amount);
    Transaction withdraw(String accountNumber, BigDecimal amount);
    Transaction transfer(String sourceAccountNumber, String destinationAccountNumber, BigDecimal amount);
}
