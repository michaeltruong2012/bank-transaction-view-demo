package org.examples.banking.domains.transaction.dto;


import java.time.LocalDate;

public record BankTransactionDto(
    String accountNumber,
    String accountName,
    LocalDate transactionDate,
    String currencyCode,
    Double debitAmount,
    Double creditAmount,
    BankTransactionType transactionType,
    String transactionNarrative
) {
}
