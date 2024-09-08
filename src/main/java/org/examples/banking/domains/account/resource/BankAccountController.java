package org.examples.banking.domains.account.resource;

import org.examples.banking.domains.account.dto.BankAccountDto;
import org.examples.banking.domains.account.service.BankAccountService;
import org.examples.banking.common.security.AuthenticationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BankAccountController {

    private final AuthenticationContext authenticationContext;
    private final BankAccountService bankAccountService;

    public BankAccountController(AuthenticationContext authenticationContext, BankAccountService bankAccountService) {
        this.authenticationContext = authenticationContext;
        this.bankAccountService = bankAccountService;
    }

    @GetMapping(path = "bank/accounts", produces = "application/json")
    public ResponseEntity<List<BankAccountDto>> getUserBankAccounts() {
        return ResponseEntity.ok(bankAccountService.getAllBankAccounts(authenticationContext.getLoggedInUser()));
    }
}
