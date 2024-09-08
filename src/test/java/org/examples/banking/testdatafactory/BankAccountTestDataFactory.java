package org.examples.banking.testdatafactory;

import org.examples.banking.common.model.BankAccountType;
import org.examples.banking.domains.account.entity.BankAccount;
import org.examples.banking.domains.user.entity.BankUser;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Random;
import java.util.function.Consumer;

public class BankAccountTestDataFactory {
    private static final Random BANK_ACCOUNT_ID_RANDOM = new Random();
    private static final Random BALANCE_RANDOM = new Random();
    private static final Random BANK_ACCOUNT_TYPE_RANDOM = new Random();
    private static final Random CURRENCY_CODE_RANDOM = new Random();
    private static final String[] SAMPLE_CURRENCY_CODES = new String[]{"AUD", "USD", "SGD", "EUR", "GBP"};

    public static BankAccount createRandomAccount(BankUser bankUser) {
        var id = BANK_ACCOUNT_ID_RANDOM.nextLong(10_000, 900_000);
        var accountNumber = "T" + id;
        var accountType = BankAccountType.values()[BANK_ACCOUNT_TYPE_RANDOM.nextInt(BankAccountType.values().length)];
        var balance = BigDecimal.valueOf(BALANCE_RANDOM.nextDouble(-1_000, 10_000));
        var currencyCode = SAMPLE_CURRENCY_CODES[CURRENCY_CODE_RANDOM.nextInt(SAMPLE_CURRENCY_CODES.length)];

        BankAccount account = new BankAccount();
        account.setId(id);
        account.setAccountNumber(accountNumber);
        account.setAccountName("Test " + accountType + " " + accountNumber);
        account.setAccountType(accountType);
        account.setAvailableBalance(balance);
        account.setBalanceDate(Instant.now());
        account.setCurrencyCode(currencyCode);
        account.setOwnerUser(bankUser);
        return account;
    }

    public static BankAccount createRandomAccount(BankUser bankUser, Consumer<BankAccount> accountCustomizer) {
        var account = createRandomAccount(bankUser);
        if (accountCustomizer != null) {
            accountCustomizer.accept(account);
        }
        return account;
    }
}
