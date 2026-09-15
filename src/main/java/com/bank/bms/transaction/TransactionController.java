package com.bank.bms.transaction;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/transactions")
@AllArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@Valid @RequestBody TransactionRequestDto request){
        Transaction transaction = transactionService.deposit(request.getDestinationAccountNumber(),request.getAmount());

        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@Valid @RequestBody TransactionRequestDto request){
        Transaction transaction = transactionService.withdraw(request.getSourceAccountNumber(), request.getAmount());

        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@Valid @RequestBody TransactionRequestDto request){
        Transaction transaction = transactionService.transfer(request.getSourceAccountNumber(),request.getDestinationAccountNumber(),request.getAmount());

        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }
}
