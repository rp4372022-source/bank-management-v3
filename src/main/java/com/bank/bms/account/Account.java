package com.bank.bms.account;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import com.bank.bms.user.User;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor //Hibernate requires a no-argument constructor
// to instantiate objects when reading data from the database
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING) //so they save as cleaner words
    // (like "SAVINGS") in MySQL rather than numbers (0, 1).
    private Status status;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
}
