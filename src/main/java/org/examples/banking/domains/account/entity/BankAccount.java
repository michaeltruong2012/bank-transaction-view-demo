package org.examples.banking.domains.account.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.examples.banking.common.model.BankAccountType;
import org.examples.banking.domains.user.entity.BankUser;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "BANK_ACCOUNT")
public class BankAccount {

    @Id
    @GeneratedValue
    @Column(name = "bank_account_id")
    private Long id;

    @Column(name = "swift_code", nullable = false)
    private String swiftCode;

    @Column(name = "bsb", nullable = false)
    private String bsb;

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;

    @ManyToOne
    @JoinColumn(name = "owner_user_id", nullable = false)
    private BankUser ownerUser;

    @Column(name = "account_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BankAccountType accountType;

    @Column(name = "currency_code", nullable = false)
    private String currencyCode;

    @Column(name = "avail_balance", nullable = false)
    private BigDecimal availableBalance;

    @Column(name = "balance_date", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant balanceDate;

    @Column(name = "created_date", nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant createdDate;

    @Column(name = "updated_date", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant updatedDate;

    public BankAccount() {
        // The JPA specification requires that all persistent classes have a no-arg constructor
    }

    public BankAccount(String swiftCode, String bsb,
                       String accountName, String accountNumber,
                       BankUser ownerUser, BankAccountType accountType,
                       BigDecimal availableBalance, String currencyCode) {
        this.swiftCode = swiftCode;
        this.bsb = bsb;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.ownerUser = ownerUser;
        this.accountType = accountType;
        this.availableBalance = availableBalance;
        this.currencyCode = currencyCode;

        this.balanceDate = Instant.now();
        this.createdDate = Instant.now();
        this.updatedDate = Instant.now();
    }

    public BankUser getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(BankUser ownerUser) {
        this.ownerUser = ownerUser;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getBsb() {
        return bsb;
    }

    public void setBsb(String bsb) {
        this.bsb = bsb;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BankAccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(BankAccountType accountType) {
        this.accountType = accountType;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public Instant getBalanceDate() {
        return balanceDate;
    }

    public void setBalanceDate(Instant balanceDate) {
        this.balanceDate = balanceDate;
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
