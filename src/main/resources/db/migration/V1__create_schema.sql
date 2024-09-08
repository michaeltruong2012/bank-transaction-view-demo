-- CREATE SEQUENCE bank_user_seq START WITH 1 INCREMENT BY 50;

-- BANK USER --

CREATE TABLE bank_user
(
    user_id   BIGINT       NOT NULL,
    user_name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_bankuser PRIMARY KEY (user_id)
);

ALTER TABLE bank_user
    ADD CONSTRAINT uc_bankuser_user_name UNIQUE (user_name);

-- BANK ACCOUNT --

CREATE TABLE bank_account
(
    bank_account_id BIGINT                   NOT NULL,
    swift_code      VARCHAR(10)              NOT NULL,
    bsb             VARCHAR(10)              NOT NULL,
    account_name    VARCHAR(255)             NOT NULL,
    account_number  VARCHAR(50)              NOT NULL,
    account_type    VARCHAR(255)             NOT NULL,
    owner_user_id   BIGINT                   NOT NULL,
    currency_code   VARCHAR(6)               NOT NULL,
    avail_balance   DECIMAL                  NOT NULL,
    balance_date    TIMESTAMP WITH TIME ZONE NOT NULL,
    created_date    TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_date    TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT pk_bank_account PRIMARY KEY (bank_account_id)
);

ALTER TABLE bank_account
    ADD CONSTRAINT uc_bank_account_account_number UNIQUE (account_number);

ALTER TABLE bank_account
    ADD CONSTRAINT FK_BANK_ACCOUNT_ON_OWNER_USER FOREIGN KEY (owner_user_id) REFERENCES bank_user (user_id);

-- BANK TRANSACTION --

CREATE TABLE bank_transaction
(
    bank_tx_id      BIGINT                   NOT NULL,
    bank_account_id BIGINT                   NULL,
    tx_date         TIMESTAMP WITH TIME ZONE NOT NULL,
    tx_amount       DECIMAL                  NOT NULL,
    tx_description  VARCHAR(255),
    created_date    TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_date    TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT pk_bank_transaction PRIMARY KEY (bank_tx_id)
);

ALTER TABLE bank_transaction
    ADD CONSTRAINT FK_BANK_TRANSACTION_ON_BANK_ACCOUNT FOREIGN KEY (bank_account_id) REFERENCES bank_account (bank_account_id);
