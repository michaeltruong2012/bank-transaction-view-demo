package org.examples.banking.domains.transaction.service;

import org.examples.banking.common.security.ApplicationUser;
import org.examples.banking.domains.transaction.dto.BankTransactionDto;
import org.examples.banking.domains.transaction.dto.BankTransactionType;
import org.examples.banking.domains.transaction.entity.BankTransaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;

@Component
public class BankTransactionDataMapper {

    public BankTransactionDto toBankTransactionDto(BankTransaction transaction, ApplicationUser user) {
        var account = transaction.getBankAccount();
        var transactionAmount = transaction.getAmount();
        var transactionType = switch (transactionAmount.compareTo(BigDecimal.ZERO)) {
            case -1 -> BankTransactionType.DEBIT;
            case 1 -> BankTransactionType.CREDIT;
            case 0 -> BankTransactionType.ZERO_VALUE;
            default ->
                throw new IllegalStateException("Unexpected value: " + transactionAmount.compareTo(BigDecimal.ZERO));
        };

        return new BankTransactionDto(
            account.getAccountNumber(),
            account.getAccountName(),
            LocalDate.ofInstant(transaction.getTransactionDate(), ZoneId.of(user.timezoneId())),
            account.getCurrencyCode(),
            transactionType == BankTransactionType.DEBIT ? transactionAmount.doubleValue() : null,
            transactionType == BankTransactionType.CREDIT ? transactionAmount.doubleValue() : null,
            transactionType,
            transaction.getDescription()
        );
    }
}
