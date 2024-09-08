package org.examples.banking.testdatafactory;

import org.examples.banking.domains.account.entity.BankAccount;
import org.examples.banking.domains.transaction.entity.BankTransaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Random;

public class BankTransactionTestDataFactory {
    private static final Random RANDOM = new Random();

    public static BankTransaction createRandomTransaction(BankAccount bankAccount) {
        return new BankTransaction(
            bankAccount,
            Instant.now(),
            BigDecimal.valueOf(RANDOM.nextDouble(-1_000, 10_000)),
            null
        );
    }
}
