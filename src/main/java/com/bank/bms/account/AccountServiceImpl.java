package com.bank.bms.account;

import com.bank.bms.user.User;
import com.bank.bms.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService{
    private static final SecureRandom secureRandom = new SecureRandom();
    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private String generateAccountNumber(){
        String accountNumber;
        boolean isDuplicate;
        do {
            StringBuilder tempAN = new StringBuilder(10);
            for(int i=0;i<10;i++){
                //generate num from 0 to 9
                tempAN.append(secureRandom.nextInt(10));
            }
            accountNumber = tempAN.toString();
            isDuplicate = accountRepository.existsByAccountNumber(accountNumber);
        } while(isDuplicate);
        return accountNumber;
    }
    @Override
    public Account createAccount(Long user_id, AccountType accountType, BigDecimal initialDeposit){

        User existing = userRepository.findById(user_id).
                orElseThrow(()-> new RuntimeException("User not found"));

        if(accountType == AccountType.SAVINGS){
            if(initialDeposit.compareTo(new BigDecimal(100)) >= 0){
                //create new Account
                Account account = new Account(
                        null,
                        generateAccountNumber(),
                        initialDeposit,
                        Status.ACTIVE,
                        AccountType.SAVINGS,
                        existing
                );
                return accountRepository.save(account);
            } else {
                throw new RuntimeException("Minimum balance for savings is $100");
            }
        }
        else {
            //create checking account
            Account account = new Account(
                    null,
                    generateAccountNumber(),
                    initialDeposit,
                    Status.ACTIVE,
                    AccountType.CHECKING,
                    existing
            );
            return accountRepository.save(account);
        }
    }
}
