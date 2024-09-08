package org.examples.banking.domains.transaction.service;

import org.examples.banking.common.security.ApplicationUser;
import org.examples.banking.domains.account.entity.BankAccount;
import org.examples.banking.domains.transaction.dto.BankTransactionDto;
import org.examples.banking.domains.transaction.dto.BankTransactionType;
import org.examples.banking.domains.transaction.entity.BankTransaction;
import org.examples.banking.domains.user.entity.BankUser;
import org.examples.banking.testdatafactory.BankAccountTestDataFactory;
import org.examples.banking.testdatafactory.BankTransactionTestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BankTransactionDataMapperTest {

    private ApplicationUser applicationUser = new ApplicationUser("test", ZoneId.systemDefault().getId());
    private final BankAccount bankAccount = BankAccountTestDataFactory.createRandomAccount(new BankUser(applicationUser.username()));
    private final BankTransactionDataMapper dataMapper = new BankTransactionDataMapper();

    @Test
    @DisplayName("should populate account data")
    public void shouldPopulateAccountData() {
        BankTransaction transaction = BankTransactionTestDataFactory.createRandomTransaction(bankAccount);

        var result = dataMapper.toBankTransactionDto(transaction, applicationUser);

        assertThat(result)
            .isNotNull()
            .satisfies(dto -> {
                assertThat(dto.accountName()).isEqualTo(bankAccount.getAccountName());
                assertThat(dto.accountNumber()).isEqualTo(bankAccount.getAccountNumber());
                assertThat(dto.currencyCode()).isEqualTo(bankAccount.getCurrencyCode());
            });
    }

    @Test
    @DisplayName("should populate transaction narrative")
    public void shouldPopulateTransactionNarrative() {
        BankTransaction transaction = BankTransactionTestDataFactory.createRandomTransaction(bankAccount);
        transaction.setDescription("description");

        var result = dataMapper.toBankTransactionDto(transaction, applicationUser);

        assertThat(result)
            .isNotNull()
            .extracting(BankTransactionDto::transactionNarrative).isEqualTo("description");
    }

    @Test
    @DisplayName("should populate transaction date")
    public void shouldPopulateTransactionDate() {
        applicationUser = new ApplicationUser("test", ZoneId.of("GMT+10").getId());
        BankTransaction transaction = BankTransactionTestDataFactory.createRandomTransaction(bankAccount);
        transaction.setTransactionDate(ZonedDateTime.parse("2024-05-03T15:15:30+01:00").toInstant());

        var result = dataMapper.toBankTransactionDto(transaction, applicationUser);

        assertThat(result)
            .isNotNull()
            .extracting(BankTransactionDto::transactionDate)
            .isEqualTo(LocalDate.parse("2024-05-04")); // next day (time is 0:15am GMT+10)
    }

    @Test
    @DisplayName("should populate amount and transaction type 'CREDIT'")
    public void shouldPopulateAmountAndCreditTransactionType() {
        BankTransaction transaction = BankTransactionTestDataFactory.createRandomTransaction(bankAccount);
        transaction.setAmount(BigDecimal.valueOf(500d));

        var result = dataMapper.toBankTransactionDto(transaction, applicationUser);

        assertThat(result)
            .isNotNull()
            .satisfies(dto -> {
                assertThat(dto.creditAmount()).isEqualTo(transaction.getAmount().doubleValue());
                assertThat(dto.debitAmount()).isNull();
                assertThat(dto.transactionType()).isEqualTo(BankTransactionType.CREDIT);
            });
    }

    @Test
    @DisplayName("should populate amount and transaction type 'DEBIT'")
    public void shouldPopulateAmountAndDebitTransactionType() {
        BankTransaction transaction = BankTransactionTestDataFactory.createRandomTransaction(bankAccount);
        transaction.setAmount(BigDecimal.valueOf(-200d));

        var result = dataMapper.toBankTransactionDto(transaction, applicationUser);

        assertThat(result)
            .isNotNull()
            .satisfies(dto -> {
                assertThat(dto.creditAmount()).isNull();
                assertThat(dto.debitAmount()).isEqualTo(transaction.getAmount().doubleValue());
                assertThat(dto.transactionType()).isEqualTo(BankTransactionType.DEBIT);
            });
    }

    @Test
    @DisplayName("should populate amount and transaction type 'NO VALUE'")
    public void shouldPopulateAmountAndZeroValueTransactionType() {
        BankTransaction transaction = BankTransactionTestDataFactory.createRandomTransaction(bankAccount);
        transaction.setAmount(BigDecimal.ZERO);

        var result = dataMapper.toBankTransactionDto(transaction, applicationUser);

        assertThat(result)
            .isNotNull()
            .satisfies(dto -> {
                assertThat(dto.creditAmount()).isNull();
                assertThat(dto.debitAmount()).isNull();
                assertThat(dto.transactionType()).isEqualTo(BankTransactionType.ZERO_VALUE);
            });
    }
}
