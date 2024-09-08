package org.examples.banking.sampledata;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.examples.banking.domains.account.dao.BankAccountRepository;
import org.examples.banking.domains.account.entity.BankAccount;
import org.examples.banking.common.model.BankAccountType;
import org.examples.banking.domains.transaction.dao.BankTransactionRepository;
import org.examples.banking.domains.transaction.entity.BankTransaction;
import org.examples.banking.domains.user.dao.BankUserRepository;
import org.examples.banking.domains.user.entity.BankUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * A simple component that generates some sample data for demo purpose.
 * <p>
 * NOTE: Because this component is used solely for creating test data, no test is created for it.
 */
@Component
public class SampleDataGenerator {
    private static final String[] SAMPLE_CURRENCY_CODES = new String[]{"AUD", "USD", "SGD", "EUR", "GBP"};
    private static final Logger LOG = LoggerFactory.getLogger(SampleDataGenerator.class);

    private final BankUserRepository bankUserRepository;
    private final BankAccountRepository bankAccountRepository;
    private final BankTransactionRepository bankTransactionRepository;

    public SampleDataGenerator(BankUserRepository bankUserRepository,
                               BankAccountRepository bankAccountRepository,
                               BankTransactionRepository bankTransactionRepository) {
        this.bankUserRepository = bankUserRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.bankTransactionRepository = bankTransactionRepository;
    }

    @PostConstruct
    @Transactional
    public void generate() {
        var bankUser = bankUserRepository.save(new BankUser("michael.truong"));
        LOG.info("Generated bank user: '{}' (id={})", bankUser.getUsername(), bankUser.getId());

        var bankAccounts = generateBankAccounts(bankUser);
        var bankAccountNumbers = bankAccounts.stream().map(BankAccount::getAccountNumber).toList();
        LOG.info("Generated {} bank accounts. Account numbers: {}", bankAccounts.size(), bankAccountNumbers);

        Random transactionCountRandom = new Random();
        bankAccounts.forEach(bankAccount -> {
            var txCount = transactionCountRandom.nextInt(0, 100);
            var transactions = generateBankTransactions(bankAccount, txCount);
            LOG.info(
                "Generated {} transactions for bank account '{}' (id={})",
                transactions.size(), bankAccount.getAccountNumber(), bankAccount.getId()
            );
        });
    }

    private List<BankTransaction> generateBankTransactions(BankAccount bankAccount, int transactionCount) {
        Random random = new Random();
        Random transactionDateRandom = new Random();

        return IntStream.range(0, transactionCount).boxed()
            .map(index -> {
                var amount = BigDecimal.valueOf(random.nextDouble(-1_000, 1000));
                var isPositiveAmount = (amount.compareTo(BigDecimal.ZERO) > 0);
                return bankTransactionRepository.save(new BankTransaction(
                    bankAccount,
                    Instant.now().minus(transactionDateRandom.nextLong(0, 1000), ChronoUnit.HOURS),
                    amount,
                    (isPositiveAmount ? "Deposited" : "Withdrawn")
                        + " " + amount.toPlainString() + " (" + bankAccount.getCurrencyCode() + ") "
                        + (isPositiveAmount ? "to" : "from") + " account " + bankAccount.getAccountNumber()
                ));
            }).peek(bankTransaction -> {
                bankAccount.setAvailableBalance(bankAccount.getAvailableBalance().add(bankTransaction.getAmount()));
                bankAccount.setBalanceDate(Instant.now());
                bankAccountRepository.save(bankAccount);
            }).toList();
    }

    private List<BankAccount> generateBankAccounts(BankUser bankUser) {
        Random accountNumberRandom = new Random();
        Random balanceRandom = new Random();
        Random bankAccountTypeRandom = new Random();
        Random currencyCodeRandom = new Random();
        return IntStream.range(0, 10).boxed().map(index -> bankAccountRepository.save(
            new BankAccount(
                "MACQAU2S",
                "182909",
                bankUser.getUsername(),
                "M" + accountNumberRandom.nextInt(10_000_000),
                bankUser,
                BankAccountType.values()[bankAccountTypeRandom.nextInt(BankAccountType.values().length)],
                new BigDecimal(balanceRandom.nextInt(100_000)),
                SAMPLE_CURRENCY_CODES[currencyCodeRandom.nextInt(0, SAMPLE_CURRENCY_CODES.length)]
            )
        )).toList();
    }
}
