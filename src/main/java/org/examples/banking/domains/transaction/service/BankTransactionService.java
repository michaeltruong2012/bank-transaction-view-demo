package org.examples.banking.domains.transaction.service;

import org.examples.banking.common.security.ApplicationUser;
import org.examples.banking.domains.account.dao.BankAccountRepository;
import org.examples.banking.domains.transaction.dao.BankTransactionRepository;
import org.examples.banking.domains.transaction.dto.BankTransactionDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankTransactionService {

    private final BankAccountRepository bankAccountRepository;
    private final BankTransactionRepository bankTransactionRepository;
    private final BankTransactionDataMapper bankTransactionDataMapper;

    public BankTransactionService(BankAccountRepository bankAccountRepository,
                                  BankTransactionRepository bankTransactionRepository,
                                  BankTransactionDataMapper bankTransactionDataMapper) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankTransactionRepository = bankTransactionRepository;
        this.bankTransactionDataMapper = bankTransactionDataMapper;
    }

    public List<BankTransactionDto> findAllByAccountNumber(String accountNumber, ApplicationUser user, int pageNumber, int pageSize) {
        return bankAccountRepository.findOneByAccountNumber(accountNumber)
            .map(bankAccount -> bankTransactionRepository.findAllByBankAccountOrderByTransactionDateDesc(bankAccount, PageRequest.of(pageNumber, pageSize)))
            .orElse(List.of()) // returning empty list if account number doesn't exist (may change logic to throw "not found" error)
            .stream()
            .map(it -> bankTransactionDataMapper.toBankTransactionDto(it, user))
            .toList();
    }
}
