package com.bank.bms.account;

import java.math.BigDecimal;

public interface AccountService {
    public Account createAccount(Long user_id, AccountType accountType, BigDecimal initialDeposit);
}
