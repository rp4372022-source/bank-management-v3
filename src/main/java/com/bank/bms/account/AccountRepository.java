package com.bank.bms.account;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //no need as we extend JpaRepo
//But it catches low level database specific SQL errors and translate them to see so put ot
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(String accountNumber);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Account> findByAccountNumber(String accountNumber);
}
