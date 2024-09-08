package org.examples.banking.domains.transaction.dao;


import org.examples.banking.domains.account.entity.BankAccount;
import org.examples.banking.domains.transaction.entity.BankTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long> {

    List<BankTransaction> findAllByBankAccountOrderByTransactionDateDesc(BankAccount bankAccount, Pageable pageable);
    long countByBankAccount(BankAccount bankAccount);
}
