package org.examples.banking.domains.account.dao;

import org.examples.banking.domains.account.entity.BankAccount;
import org.examples.banking.domains.user.entity.BankUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {

    List<BankAccount> findAllByOwnerUser(BankUser bankUser);
    Optional<BankAccount> findOneByAccountNumber(String accountNumber);
}
