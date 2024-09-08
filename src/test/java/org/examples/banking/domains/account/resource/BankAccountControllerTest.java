package org.examples.banking.domains.account.resource;

import org.examples.banking.common.model.BankAccountType;
import org.examples.banking.common.security.ApplicationUser;
import org.examples.banking.common.security.AuthenticationContext;
import org.examples.banking.domains.account.dto.BankAccountDto;
import org.examples.banking.domains.account.service.BankAccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankAccountControllerTest {

    @Mock
    private AuthenticationContext authenticationContext;

    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private BankAccountController bankAccountController;

    @Test
    @DisplayName("should fetch bank accounts for logged-in user")
    public void shouldFetchAllAccounts() {
        var appUser = new ApplicationUser("test", ZoneId.systemDefault().getId());
        when(authenticationContext.getLoggedInUser()).thenReturn(appUser);
        when(bankAccountService.getAllBankAccounts(appUser)).thenReturn(
            List.of(
                new BankAccountDto(
                    "test", "12345", BankAccountType.CREDIT, "USD", 1_000D, LocalDate.now()
                ),
                new BankAccountDto(
                    "test2", "12346", BankAccountType.SAVING, "USD", 1_200D, LocalDate.now()
                )
            )
        );

        var result = bankAccountController.getUserBankAccounts();

        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getBody()).hasSize(2);
        assertThat(result.getBody()).extracting(BankAccountDto::accountNumber).contains("12345", "12346");
    }

    @Test
    @DisplayName("should fetch bank accounts ONLY for logged-in user")
    public void shouldFetchAccountsOnlyForLoggedInUser() {
        var appUser = new ApplicationUser("test", ZoneId.systemDefault().getId());
        var appUserHavingAccounts = new ApplicationUser("test-with-accounts", ZoneId.systemDefault().getId());
        when(authenticationContext.getLoggedInUser()).thenReturn(appUser);
        when(bankAccountService.getAllBankAccounts(not(eq(appUserHavingAccounts)))).thenReturn(List.of());

        var result = bankAccountController.getUserBankAccounts();

        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getBody()).isEmpty();
    }
}
