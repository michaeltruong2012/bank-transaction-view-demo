package org.examples.banking.domains.account.dto;

import org.examples.banking.common.model.BankAccountType;

import java.time.LocalDate;

public record BankAccountDto(
    String accountName,
    String accountNumber,
    BankAccountType accountType,
    String currencyCode,
    Double availableBalance,
    LocalDate balanceLocalDate) {
}
