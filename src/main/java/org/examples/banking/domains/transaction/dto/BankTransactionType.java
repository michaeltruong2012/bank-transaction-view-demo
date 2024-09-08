package org.examples.banking.domains.transaction.dto;


public enum BankTransactionType {

    /**
     * Transaction type: credit money to account
     */
    CREDIT,

    /**
     * Transaction type: debit money from account
     */
    DEBIT,

    /**
     * Transaction type: no transaction value, just "informational" transaction (e.g. announcement of interest rate increase)
     */
    ZERO_VALUE
}
