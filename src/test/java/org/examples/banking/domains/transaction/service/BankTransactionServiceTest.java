package org.examples.banking.domains.transaction.service;

import org.examples.banking.common.security.ApplicationUser;
import org.examples.banking.domains.account.dao.BankAccountRepository;
import org.examples.banking.domains.account.entity.BankAccount;
import org.examples.banking.domains.transaction.dao.BankTransactionRepository;
import org.examples.banking.domains.user.entity.BankUser;
import org.examples.banking.testdatafactory.BankAccountTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.examples.banking.testdatafactory.BankTransactionTestDataFactory.createRandomTransaction;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankTransactionServiceTest {

    @InjectMocks
    private BankTransactionService bankTransactionService;

    @Mock
    private BankTransactionRepository bankTransactionRepository;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Spy
    private BankTransactionDataMapper bankTransactionDataMapper;

    private final ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);

    private BankAccount bankAccount;
    private final ApplicationUser applicationUser = new ApplicationUser("test", ZoneId.systemDefault().getId());

    @BeforeEach
    void setUp() {
        bankAccount = BankAccountTestDataFactory.createRandomAccount(new BankUser("test"));
    }

    @Test
    @DisplayName("should fetch all transactions for a given account")
    public void shouldFetchTransactionsForBankAccount() {
        when(bankAccountRepository.findOneByAccountNumber(bankAccount.getAccountNumber())).thenReturn(Optional.of(bankAccount));
        when(bankTransactionRepository.findAllByBankAccountOrderByTransactionDateDesc(eq(bankAccount), any())).thenReturn(
            List.of(
                createRandomTransaction(bankAccount),
                createRandomTransaction(bankAccount),
                createRandomTransaction(bankAccount)
            )
        );

        var results = bankTransactionService.findAllByAccountNumber(bankAccount.getAccountNumber(), applicationUser, 0, 10);

        assertThat(results).hasSize(3);
    }

    @Test
    @DisplayName("should fetch transactions using given page number and page size")
    public void shouldFetchTransactionsAccordingToGivenPageNumberAndPageSize() {
        when(bankAccountRepository.findOneByAccountNumber(bankAccount.getAccountNumber())).thenReturn(Optional.of(bankAccount));
        when(bankTransactionRepository.findAllByBankAccountOrderByTransactionDateDesc(eq(bankAccount), any())).thenReturn(
            List.of(
                createRandomTransaction(bankAccount),
                createRandomTransaction(bankAccount),
                createRandomTransaction(bankAccount)
            )
        );

        bankTransactionService.findAllByAccountNumber(bankAccount.getAccountNumber(), applicationUser, 0, 10);

        verify(bankTransactionRepository).findAllByBankAccountOrderByTransactionDateDesc(eq(bankAccount), pageableArgumentCaptor.capture());
        var pageable = pageableArgumentCaptor.getValue();
        assertThat(pageable.getPageNumber()).isEqualTo(0);
        assertThat(pageable.getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("should not fetch transactions when account does not exist")
    public void shouldNotFetchTransactionsWhenAccountNotFound() {
        when(bankAccountRepository.findOneByAccountNumber(bankAccount.getAccountNumber())).thenReturn(Optional.empty());

        var results = bankTransactionService.findAllByAccountNumber(bankAccount.getAccountNumber(), applicationUser, 0, 10);

        assertThat(results).isEmpty();
    }
}
