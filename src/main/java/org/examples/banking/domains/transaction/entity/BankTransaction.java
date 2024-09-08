package org.examples.banking.domains.transaction.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.examples.banking.domains.account.entity.BankAccount;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "BANK_TRANSACTION")
public class BankTransaction {

    @Id
    @GeneratedValue
    @Column(name = "bank_tx_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;

    @Column(name = "tx_date",nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant transactionDate;

    @Column(name = "tx_amount",nullable = false)
    private BigDecimal amount; // positive = credit, negative = debit

    @Column(name = "tx_description")
    private String description; // transaction narrative

    @Column(name = "created_date",nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant createdDate;

    @Column(name = "updated_date",nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant updatedDate;

    public BankTransaction() {
        // The JPA specification requires that all persistent classes have a no-arg constructor
    }

    public BankTransaction(BankAccount bankAccount, Instant transactionDate, BigDecimal amount, String description) {
        this.bankAccount = bankAccount;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.description = description;

        this.createdDate = Instant.now();
        this.updatedDate = Instant.now();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Instant getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Instant transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }
}
