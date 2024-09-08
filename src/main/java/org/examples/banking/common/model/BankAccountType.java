package org.examples.banking.common.model;

public enum BankAccountType {
    SAVING("Savings"),
    TRANSACTION("Current"),
    CREDIT("Credit");

    private final String displayName;

    BankAccountType(String name) {
        this.displayName = name;
    }

    public String getDisplayName() {
        return displayName;
    }
}
