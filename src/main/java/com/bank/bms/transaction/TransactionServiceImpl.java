package com.bank.bms.transaction;

import com.bank.bms.account.Account;
import com.bank.bms.account.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService{
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String PREFIX = "TNX-";
    private static final SecureRandom secureRandom = new SecureRandom();
    //to generate Transaction Reference Numbe
    private String generateTRN(){
        String trn;
        boolean isTRNExists;
        StringBuilder tempTRN = new StringBuilder(PREFIX);
        do {
            for(int i=0;i<10;i++){
                int index = secureRandom.nextInt(CHARACTERS.length());
                tempTRN.append(CHARACTERS.charAt(index));
            }
            trn = tempTRN.toString();
            isTRNExists = transactionRepository.existsByTransactionReference(trn);
        } while(isTRNExists);

        return trn;
    }
    @Transactional //If your code successfully updates the Account balance, but then your database server crashes right before saving the Transaction ledger entry, Spring will automatically catch the failure and roll back the balance update.The database will act as if the balance change never happened, guaranteeing Atomicity (All or Nothing).
    @Override
    public Transaction deposit(String accountNumber, BigDecimal amount){
        //check if account num exist
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account number not found"));
        //verify amount
        if(amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new RuntimeException("Amount must be greater than zero");
        else {
            //fetch and update balance
            BigDecimal balance = account.getBalance();
            balance = balance.add(amount);
            account.setBalance(balance);

            //update in accounts table
            accountRepository.save(account);

            //generate TRN
            String trn = generateTRN();
            //create save and return transaction
            Transaction currentTransaction = new Transaction(
                    null,
                    trn,
                    null,
                    accountNumber,
                    amount,
                    TransactionType.DEPOSIT,
                    LocalDateTime.now()
            );

            return transactionRepository.save(currentTransaction);
        }
    }
    @Transactional
    @Override
    public Transaction withdraw(String accountNumber, BigDecimal amount){
        //check if account num exist
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account number not found"));

        //fetch verify and update balance
        BigDecimal balance = account.getBalance();
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("withdrawal amount less than zero");
        }
        else if(amount.compareTo(balance) > 0)
            throw new RuntimeException("withdrawal amount is greater than balance");
        else {
            balance = balance.subtract(amount);
            account.setBalance(balance);

            //update in accounts table
            accountRepository.save(account);

            //create save and return transaction
            Transaction currentTransaction = new Transaction(
                    null,
                    generateTRN(),
                    accountNumber,
                    null,
                    amount,
                    TransactionType.WITHDRAWAL,
                    LocalDateTime.now()
            );

            return transactionRepository.save(currentTransaction);
        }
    }
    @Transactional
    @Override
    public Transaction transfer(String sourceAccountNumber, String destinationAccountNumber, BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("Transfer amount less than or equal to zero");
        }
        if(sourceAccountNumber.equals(destinationAccountNumber)){
            throw new RuntimeException("Source and destination account numbers are same");
        }

        Account firstLockAccount;
        Account secondLockAccount;

        if(sourceAccountNumber.compareTo(destinationAccountNumber) < 0){
            firstLockAccount = accountRepository.findByAccountNumber(sourceAccountNumber)
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            secondLockAccount = accountRepository.findByAccountNumber(destinationAccountNumber)
                    .orElseThrow(() -> new RuntimeException("Account not found"));
        } else {
            firstLockAccount = accountRepository.findByAccountNumber(destinationAccountNumber)
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            secondLockAccount = accountRepository.findByAccountNumber(sourceAccountNumber)
                    .orElseThrow(() -> new RuntimeException("Account not found"));
        }

        Account sourceAccount = sourceAccountNumber.equals(firstLockAccount.getAccountNumber()) ? firstLockAccount : secondLockAccount;
        Account destinationAccount = destinationAccountNumber.equals(firstLockAccount.getAccountNumber()) ? firstLockAccount : secondLockAccount;

        //business logic
        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds in account: " + sourceAccountNumber);
        }
        //adjust balance
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        destinationAccount.setBalance(destinationAccount.getBalance().add(amount));

        //save changes
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        Transaction transaction = new Transaction(
                null,
                generateTRN(),
                sourceAccountNumber,
                destinationAccountNumber,
                amount,
                TransactionType.TRANSFER,
                LocalDateTime.now()
        );

        return transactionRepository.save(transaction);
    }
}
