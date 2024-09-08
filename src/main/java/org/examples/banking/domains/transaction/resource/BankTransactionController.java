package org.examples.banking.domains.transaction.resource;

import org.examples.banking.common.security.AuthenticationContext;
import org.examples.banking.domains.transaction.service.BankTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankTransactionController {

    private final AuthenticationContext authenticationContext;
    private final BankTransactionService bankTransactionService;

    public BankTransactionController(AuthenticationContext authenticationContext, BankTransactionService bankTransactionService) {
        this.authenticationContext = authenticationContext;
        this.bankTransactionService = bankTransactionService;
    }

    @GetMapping(path = "bank/transactions/{accountNumber}", produces = "application/json")
    public ResponseEntity<?> getUserAccountTransactions(
        @PathVariable("accountNumber") String accountNumber,
        @RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
        @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        if (pageNumber < 0) {
            return ResponseEntity.badRequest().body("Invalid page number: Page size must not be negative");
        }

        if (pageSize <= 0) {
            return ResponseEntity.badRequest().body("Invalid page size: Page size must not be less than one");
        }

        return ResponseEntity.ok(bankTransactionService.findAllByAccountNumber(accountNumber, authenticationContext.getLoggedInUser(), pageNumber, pageSize));
    }
}
