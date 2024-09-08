package org.examples.banking.domains.account.service;

import org.examples.banking.common.exception.UserNotFoundException;
import org.examples.banking.common.security.ApplicationUser;
import org.examples.banking.domains.account.dao.BankAccountRepository;
import org.examples.banking.domains.account.entity.BankAccount;
import org.examples.banking.domains.account.dto.BankAccountDto;
import org.examples.banking.domains.user.dao.BankUserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class BankAccountService {

    private final BankUserRepository bankUserRepository;
    private final BankAccountRepository bankAccountRepository;

    public BankAccountService(BankUserRepository bankUserRepository, BankAccountRepository bankAccountRepository) {
        this.bankUserRepository = bankUserRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    public List<BankAccountDto> getAllBankAccounts(ApplicationUser user) {
        return bankUserRepository.findByUsername(user.username())
            .map(bankAccountRepository::findAllByOwnerUser)
            .map(results -> results.stream().map(it -> toBankAccountDto(it, user)).toList())
            .orElseThrow(() -> new UserNotFoundException("User " + user.username() + " does not exist"));
    }

    private BankAccountDto toBankAccountDto(BankAccount bankAccount, ApplicationUser user) {
        return new BankAccountDto(
            bankAccount.getAccountName(),
            bankAccount.getAccountNumber(),
            bankAccount.getAccountType(),
            bankAccount.getCurrencyCode(),
            bankAccount.getAvailableBalance().doubleValue(),
            LocalDate.ofInstant(bankAccount.getBalanceDate(), ZoneId.of(user.timezoneId()))
        );
    }
}
