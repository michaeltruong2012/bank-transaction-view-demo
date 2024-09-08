package org.examples.banking.domains.account.service;

import org.examples.banking.common.exception.UserNotFoundException;
import org.examples.banking.common.security.ApplicationUser;
import org.examples.banking.domains.account.dao.BankAccountRepository;
import org.examples.banking.domains.user.dao.BankUserRepository;
import org.examples.banking.domains.user.entity.BankUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.examples.banking.testdatafactory.BankAccountTestDataFactory.createRandomAccount;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceTest {

    @InjectMocks
    private BankAccountService bankAccountService;

    @Mock
    private BankUserRepository bankUserRepository;

    @Mock
    private BankAccountRepository bankAccountRepository;

    private ApplicationUser appUser;
    private BankUser testBankUser;

    @BeforeEach
    void setUp() {
        appUser = new ApplicationUser("michael.truong", ZoneId.systemDefault().getId());
        testBankUser = new BankUser(appUser.username());
    }

    @Test
    @DisplayName("should fetch bank accounts for the current app user")
    public void shouldFetchBankAccounts() {
        when(bankUserRepository.findByUsername(appUser.username())).thenReturn(Optional.of(testBankUser));
        when(bankAccountRepository.findAllByOwnerUser(testBankUser)).thenReturn(
            List.of(createRandomAccount(testBankUser), createRandomAccount(testBankUser))
        );

        var results = bankAccountService.getAllBankAccounts(appUser);

        assertThat(results).size().isEqualTo(2);
        verify(bankUserRepository).findByUsername(appUser.username());
        verify(bankAccountRepository).findAllByOwnerUser(testBankUser);
    }

    @Test
    @DisplayName("should convert database entity to corresponding DTO")
    public void shouldConvertEntityToDto() {
        appUser = new ApplicationUser("michael.truong", ZoneId.of("+10:00").getId());
        var testBankAccount = createRandomAccount(testBankUser, it -> {
            it.setBalanceDate(ZonedDateTime.parse("2007-12-03T10:15:30+10:00").toInstant());
        });
        when(bankUserRepository.findByUsername(appUser.username())).thenReturn(Optional.of(testBankUser));
        when(bankAccountRepository.findAllByOwnerUser(testBankUser)).thenReturn(List.of(testBankAccount));

        var results = bankAccountService.getAllBankAccounts(appUser);

        assertThat(results).size().isEqualTo(1);
        assertThat(results).element(0).satisfies(dto -> {
            assertThat(dto.accountName()).isEqualTo(testBankAccount.getAccountName());
            assertThat(dto.accountNumber()).isEqualTo(testBankAccount.getAccountNumber());
            assertThat(dto.accountType()).isEqualTo(testBankAccount.getAccountType());
            assertThat(dto.availableBalance()).isEqualTo(testBankAccount.getAvailableBalance().doubleValue());
            assertThat(dto.currencyCode()).isEqualTo(testBankAccount.getCurrencyCode());
            assertThat(dto.balanceLocalDate()).isEqualTo("2007-12-03");
        });
    }

    @Test
    @DisplayName("should throw error when app user does not exist in database")
    public void shouldThrowErrorWhenUserDoesNotExistInDatabase() {
        when(bankUserRepository.findByUsername(appUser.username())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bankAccountService.getAllBankAccounts(appUser))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessage("User michael.truong does not exist");

        verify(bankUserRepository).findByUsername(appUser.username());
        verifyNoInteractions(bankAccountRepository);
    }

    @Test
    @DisplayName("should not return any account when user does not have any bank account")
    public void shouldNotReturnAnyAccountWhenUserDoesNotHaveAccount() {
        when(bankUserRepository.findByUsername(appUser.username())).thenReturn(Optional.of(testBankUser));
        when(bankAccountRepository.findAllByOwnerUser(testBankUser)).thenReturn(List.of());

        var results = bankAccountService.getAllBankAccounts(appUser);

        assertThat(results).isEmpty();
        verify(bankUserRepository).findByUsername(appUser.username());
        verify(bankAccountRepository).findAllByOwnerUser(testBankUser);
    }
}
