package org.examples.banking;

import org.examples.banking.common.security.AuthenticationContext;
import org.examples.banking.domains.account.dao.BankAccountRepository;
import org.examples.banking.domains.account.dto.BankAccountDto;
import org.examples.banking.domains.account.entity.BankAccount;
import org.examples.banking.domains.transaction.dao.BankTransactionRepository;
import org.examples.banking.domains.transaction.dto.BankTransactionDto;
import org.examples.banking.domains.transaction.entity.BankTransaction;
import org.examples.banking.domains.user.dao.BankUserRepository;
import org.examples.banking.domains.user.entity.BankUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionViewDemoApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private BankUserRepository bankUserRepository;

    @Autowired
    private BankTransactionRepository bankTransactionRepository;

    @Autowired
    private AuthenticationContext authenticationContext;

    private BankUser bankUser;

    @BeforeEach
    void setUp() {
        bankUser = bankUserRepository.findByUsername(authenticationContext.getLoggedInUser().username()).orElseThrow();
    }

    @Test
    @DisplayName("should fetch all bank accounts for current user")
    public void shouldFetchAllBankAccountsForCurrentUser() {
        var expectedEntities = bankAccountRepository.findAllByOwnerUser(bankUser);

        var results = this.restTemplate.getForObject(getBaseUrl() + "/bank/accounts", BankAccountDto[].class);
        var actualAccountNumbers = Arrays.stream(results).map(BankAccountDto::accountNumber).toList();

        assertThat(results).hasSize(expectedEntities.size());
        assertThat(actualAccountNumbers).containsAll(expectedEntities.stream().map(BankAccount::getAccountNumber).toList());
    }

    @Test
    @DisplayName("should fetch the first 10 transactions for a given bank account")
    public void shouldFetchTop10BankTransactionsForGivenBankAccount() {
        var bankAccount = bankAccountRepository.findAllByOwnerUser(bankUser).stream().findFirst().orElseThrow();
        var expectedEntities = bankTransactionRepository.findAllByBankAccountOrderByTransactionDateDesc(
            bankAccount, PageRequest.of(0, 10));

        var results = this.restTemplate.getForObject(
            getBaseUrl() + "/bank/transactions/" + bankAccount.getAccountNumber(),
            BankTransactionDto[].class);
        var actualTransactionAmounts = Arrays.stream(results)
            .map(dto -> switch (dto.transactionType()) {
                case DEBIT -> dto.debitAmount();
                case CREDIT -> dto.creditAmount();
                case ZERO_VALUE -> null;
            }).toList();

        assertThat(results).hasSize(expectedEntities.size());
        var expectedAmounts = expectedEntities.stream().map(BankTransaction::getAmount).map(BigDecimal::doubleValue).toList();
        assertThat(actualTransactionAmounts).containsSequence(expectedAmounts);
    }

    @Test
    @DisplayName("should fetch transactions from the last page for a given bank account")
    public void shouldFetchBankTransactionsFromTheLastPageForGivenBankAccount() {
        var bankAccount = bankAccountRepository.findAllByOwnerUser(bankUser).stream().findFirst().orElseThrow();
        var transactionCount = bankTransactionRepository.countByBankAccount(bankAccount);
        int pageSize = 10;
        int pageCount = ((int) transactionCount) / pageSize;
        var expectedEntities = bankTransactionRepository.findAllByBankAccountOrderByTransactionDateDesc(
            bankAccount, PageRequest.of(pageCount - 1, pageSize));

        var results = this.restTemplate.getForObject(
            getBaseUrl() + "/bank/transactions/" + bankAccount.getAccountNumber() + "?pageNumber=" + (pageCount - 1) + "&pageSize=" + pageSize,
            BankTransactionDto[].class);
        var actualTransactionAmounts = Arrays.stream(results)
            .map(dto -> switch (dto.transactionType()) {
                case DEBIT -> dto.debitAmount();
                case CREDIT -> dto.creditAmount();
                case ZERO_VALUE -> null;
            }).toList();

        assertThat(results).hasSize(expectedEntities.size());
        var expectedAmounts = expectedEntities.stream().map(BankTransaction::getAmount).map(BigDecimal::doubleValue).toList();
        assertThat(actualTransactionAmounts).containsSequence(expectedAmounts);
    }

    private String getBaseUrl() {
        return "http://localhost:" + port + "/";
    }
}
