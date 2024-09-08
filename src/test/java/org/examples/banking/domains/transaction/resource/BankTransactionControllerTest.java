package org.examples.banking.domains.transaction.resource;

import org.examples.banking.common.security.ApplicationUser;
import org.examples.banking.common.security.AuthenticationContext;
import org.examples.banking.domains.transaction.dto.BankTransactionDto;
import org.examples.banking.domains.transaction.dto.BankTransactionType;
import org.examples.banking.domains.transaction.service.BankTransactionService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankTransactionControllerTest {

    private final ApplicationUser applicationUser = new ApplicationUser("test", ZoneId.systemDefault().getId());

    @Mock
    private AuthenticationContext authenticationContext;

    @Mock
    private BankTransactionService bankTransactionService;

    @InjectMocks
    private BankTransactionController bankTransactionController;

    @Test
    @DisplayName("should fetch up to 10 most recent transactions for a given bank account number")
    public void shouldFetchUpTo10MostRecentTransactions() {
        when(authenticationContext.getLoggedInUser()).thenReturn(applicationUser);
        when(bankTransactionService.findAllByAccountNumber("test-account", applicationUser, 0, 10)).thenReturn(
            List.of(
                new BankTransactionDto(
                    "test-account",
                    null,
                    LocalDate.now(),
                    "AUD",
                    -100d,
                    null,
                    BankTransactionType.DEBIT,
                    null
                )
            )
        );

        var response = bankTransactionController.getUserAccountTransactions("test-account", 0, 10);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(authenticationContext).getLoggedInUser();
        verify(bankTransactionService).findAllByAccountNumber("test-account", applicationUser, 0, 10);
    }

    @Test
    @DisplayName("should return HTTP error 400 when page number is negative")
    public void shouldReturnError400WhenPageNumberIsNegative() {
        var response = bankTransactionController.getUserAccountTransactions("test-account", -1, 10);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        verifyNoInteractions(authenticationContext, bankTransactionService);
    }

    @Test
    @DisplayName("should return HTTP error 400 when page size is less than 1")
    public void shouldReturnError400WhenPageSizeIsLessThanOne() {
        var response = bankTransactionController.getUserAccountTransactions("test-account", 1, 0);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        verifyNoInteractions(authenticationContext, bankTransactionService);
    }
}
