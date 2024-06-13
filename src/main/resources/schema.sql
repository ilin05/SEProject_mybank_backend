CREATE DATABASE IF NOT EXISTS mybank;
USE mybank;

# 柜台
CREATE TABLE IF NOT EXISTS customer
(
    customer_id        INT PRIMARY KEY AUTO_INCREMENT,
    customer_name      VARCHAR(20)        NOT NULL,
    id_number          VARCHAR(20) UNIQUE NOT NULL,
    phone_number       VARCHAR(20)        NOT NULL,
    address            VARCHAR(50)        NOT NULL,
    credit_line        DECIMAL(20, 2) DEFAULT 0,
    assets             DECIMAL(20, 2) DEFAULT 0,
    internet_bank_open BOOLEAN        DEFAULT FALSE,
    CHECK (credit_line >= 0),
    CHECK (assets >= 0 )
);

CREATE TABLE IF NOT EXISTS saving_account
(
    account_id   NCHAR(19) PRIMARY KEY,
    customer_id  INT            NOT NULL,
    password     VARCHAR(20)    NOT NULL,
    balance      DECIMAL(20, 2) NOT NULL,
    freeze_state BOOLEAN           DEFAULT FALSE,
    loss_state   BOOLEAN           DEFAULT FALSE,
    deleted      BOOLEAN           DEFAULT FALSE,
    open_time    DATETIME       NOT NULL,
    open_amount  DECIMAL(20, 2) NOT NULL,
    interest     DECIMAL(20, 6) DEFAULT 0,
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CHECK ( balance >= 0 )
);

CREATE TABLE IF NOT EXISTS fixed_deposit_type
(
    deposit_type     VARCHAR(15) PRIMARY KEY,
    deposit_duration INT            NOT NULL,
    deposit_rate     DECIMAL(10, 6) NOT NULL
);

CREATE TABLE IF NOT EXISTS fixed_deposit
(
    fixed_deposit_id INT PRIMARY KEY AUTO_INCREMENT,
    account_id       NCHAR(19)      NOT NULL,
    deposit_time     DATETIME       NOT NULL,
    deposit_amount   DECIMAL(20, 2) NOT NULL,
    deposit_type     VARCHAR(10)    NOT NULL,
    is_renewal       BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (account_id) REFERENCES saving_account (account_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (deposit_type) REFERENCES fixed_deposit_type (deposit_type)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CHECK ( deposit_amount >= 0 )
);

CREATE TABLE IF NOT EXISTS transaction
(
    transaction_id      INT PRIMARY KEY AUTO_INCREMENT,
    card_id             NCHAR(19)      NOT NULL,
    card_type           VARCHAR(15)    NOT NULL,
    transaction_time    DATETIME       NOT NULL,
    transaction_amount  DECIMAL(20, 2) NOT NULL,
    transaction_type    VARCHAR(20)    NOT NULL,
    transaction_channel VARCHAR(20) DEFAULT NULL,
    currency            NCHAR(3)    DEFAULT 'CNY',
    money_source        NCHAR(19)   DEFAULT NULL,
    money_goes          NCHAR(19)   DEFAULT NULL,
    CHECK ( transaction_amount >= 0)
);

CREATE TABLE IF NOT EXISTS freeze_state_record
(
    freeze_state_record_id INT PRIMARY KEY AUTO_INCREMENT,
    account_id             NCHAR(19)   NOT NULL,
    freeze_time            DATETIME    NOT NULL,
    unfreeze_time          DATETIME    NOT NULL,
    freeze_reason          VARCHAR(50) NOT NULL,
    FOREIGN KEY (account_id) REFERENCES saving_account (account_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS loss_state_record
(
    loss_state_record_id INT PRIMARY KEY AUTO_INCREMENT,
    account_id           NCHAR(19) NOT NULL,
    loss_time            DATETIME  NOT NULL,
    reissue_time         DATETIME DEFAULT NULL,
    FOREIGN KEY (account_id) REFERENCES saving_account (account_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS cashier
(
    cashier_id   INT PRIMARY KEY AUTO_INCREMENT,
    cashier_name VARCHAR(20)        NOT NULL,
    id_number    VARCHAR(20) UNIQUE NOT NULL,
    phone_number VARCHAR(20)        NOT NULL,
    password     VARCHAR(20)        NOT NULL,
    address      VARCHAR(50)        NOT NULL,
    privilege    CHAR               NOT NULL,
    CHECK ( privilege IN ('A', 'B', 'C'))
) AUTO_INCREMENT = 100001;

# 定期存款 利率表 初始化
# 存款名称 持续月份 年利率
INSERT INTO fixed_deposit_type (deposit_type, deposit_duration, deposit_rate)
VALUES ('A', 3, 0.0115), ('B', 6, 0.0135), ('C', 12, 0.0145),
       ('D', 24, 0.0165), ('E', 36, 0.0195), ('F', 60, 0.0200);

# 贷款
CREATE TABLE IF NOT EXISTS reviewer
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    name     VARCHAR(50)  NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS loan_application
(
    loan_id                   INT PRIMARY KEY AUTO_INCREMENT,
    loan_amount               DECIMAL(20, 2) NOT NULL,
    customer_id               INT            NOT NULL,
    account_id                NCHAR(19)      NOT NULL,
    status                    VARCHAR(10)    DEFAULT 'PENDING',    -- 'PENDING', 'REJECTED', 'APPROVED', 'CONFIRMED'
    reviewer_id               INT,
    interest_rate             DECIMAL(5, 2),
    loan_type                 VARCHAR(15)    NOT NULL,             -- 'MORTGAGE', 'CAR_LOAN', 'PERSONAL_LOAN', 'EDUCATION_LOAN', 'BUSINESS_LOAN', 'OTHER'
    repayment_status          VARCHAR(10)    DEFAULT 'NOT_REPAID', -- 'NOT_REPAID', 'PAID', 'OVERDUE'
    paid_amount               DECIMAL(20, 2) DEFAULT 0,
    total_repayment_amount    DECIMAL(20, 2),
    collateral_amount         DECIMAL(20, 2),
    loan_start_date           DATE,
    loan_duration             INT,
    loan_end_date             DATE,
    review_comments           TEXT,
    last_interest_update_date DATE,
    application_date          DATE,
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (account_id) REFERENCES saving_account (account_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (reviewer_id) REFERENCES reviewer (id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS repayment
(
    repayment_id     INT PRIMARY KEY AUTO_INCREMENT,
    loan_id          INT            NOT NULL,
    account_id       NCHAR(19),
    repayment_amount DECIMAL(20, 2) NOT NULL,
    repayment_date   DATE           NOT NULL,
    FOREIGN KEY (account_id) REFERENCES saving_account (account_id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    FOREIGN KEY (loan_id) REFERENCES loan_application (loan_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS transaction
(
    transaction_id      INT PRIMARY KEY AUTO_INCREMENT,
    card_id             NCHAR(19)                  NOT NULL,
    card_type           VARCHAR(15)                NOT NULL,
    transaction_time    DATETIME                   NOT NULL,
    transaction_amount  DECIMAL(20, 2)             NOT NULL,
    transaction_type    ENUM ('INCOME', 'EXPENSE') NOT NULL,
    transaction_channel VARCHAR(20) DEFAULT NULL,
    currency            NCHAR(3)    DEFAULT 'CNY',
    money_source        NCHAR(19)   DEFAULT NULL,
    money_goes          NCHAR(19)   DEFAULT NULL,
    CHECK (transaction_amount >= 0)
);

CREATE TABLE IF NOT EXISTS creditcards
(
    card_id         VARCHAR(23)    NOT NULL,
    customer_id     INT            NOT NULL,
    deposit_card_id VARCHAR(19)    NOT NULL,
    password        VARCHAR(255)   NOT NULL,
    status          INT            NOT NULL,
    credit_limit    DECIMAL(10, 2) NOT NULL,
    consumption     DECIMAL(10, 2) NOT NULL DEFAULT 0,
    PRIMARY KEY (card_id),
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    -- foreign key (deposit_card_id) references cards (card_id) on delete restrict on update cascade,
    CHECK (credit_limit >= 0 AND consumption >= 0 AND consumption <= creditcards.credit_limit)
) ENGINE=InnoDB
  CHARSET=utf8mb4;

# 外汇
CREATE TABLE IF NOT EXISTS wh_currency
(
    currency_id   INT            NOT NULL PRIMARY KEY,
    currency_name VARCHAR(15)    NOT NULL,
    currency_rate DECIMAL(15, 6) NOT NULL
);

CREATE TABLE IF NOT EXISTS wh_account
(
    user_id      INT         NOT NULL PRIMARY KEY,
    user_name    VARCHAR(20) NOT NULL,
    password     VARCHAR(20) NOT NULL,
    pay_password VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS wh_user_currency
(
    user_id     INT            NOT NULL,
    currency_id INT            NOT NULL,
    amount      DECIMAL(15, 2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES wh_account (user_id),
    FOREIGN KEY (currency_id) REFERENCES wh_currency (currency_id)
);

CREATE TABLE IF NOT EXISTS wh_transaction
(
    transaction_id   INT            NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id          INT            NOT NULL,
    currency_to_id   INT            NOT NULL,
    currency_from_id INT            NOT NULL,
    amount_to        DECIMAL(15, 2) NOT NULL,
    amount_from      DECIMAL(15, 2) NOT NULL,
    transaction_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES wh_account (user_id),
    FOREIGN KEY (currency_to_id) REFERENCES wh_currency (currency_id),
    FOREIGN KEY (currency_from_id) REFERENCES wh_currency (currency_id)
);

CREATE TABLE IF NOT EXISTS wh_operator
(
    opt_id           INT         NOT NULL PRIMARY KEY,
    opt_name         VARCHAR(50) NOT NULL,
    opt_password     VARCHAR(50) NOT NULL,
    control_currency BOOLEAN     NOT NULL DEFAULT 0,
    control_rate     BOOLEAN     NOT NULL DEFAULT 0
);

