package org.examples.banking.domains.user.dao;


import org.examples.banking.domains.user.entity.BankUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BankUserRepository extends CrudRepository<BankUser, Long> {

    Optional<BankUser> findByUsername(String username);
}
