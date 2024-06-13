create database mybank;
use mybank;

# 柜台
create table customer
(
    customer_id        int primary key auto_increment,
    customer_name      varchar(20)        NOT NULL,
    id_number          varchar(20) unique NOT NULL,
    phone_number       varchar(20)        NOT NULL,
    address            varchar(50)        NOT NULL,
    credit_line        decimal(20, 2) default 0,
    assets             decimal(20, 2) default 0,
    internet_bank_open boolean        default false,
    check (credit_line >= 0),
    check (assets >= 0 )
);

create table saving_account
(
    account_id   nchar(19) primary key,
    customer_id  int            NOT NULL,
    password     varchar(20)    NOT NULL,
    balance      decimal(20, 2) NOT NULL,
    freeze_state bool           default false,
    loss_state   bool           default false,
    deleted      bool           default false,
    open_time    datetime       NOT NULL,
    open_amount  decimal(20, 2) NOT NULL,
    interest     decimal(20, 6) default 0,
    foreign key (customer_id) references customer (customer_id)
        on delete cascade
        on update cascade,
    check ( balance >= 0 )
);

create table fixed_deposit_type
(
    deposit_type     varchar(15) primary key,
    deposit_duration int            NOT NULL,
    deposit_rate     decimal(10, 6) NOT NULL
);

create table fixed_deposit
(
    fixed_deposit_id int primary key auto_increment,
    account_id       nchar(19)      NOT NULL,
    deposit_time     datetime       NOT NULL,
    deposit_amount   decimal(20, 2) NOT NULL,
    deposit_type     varchar(10)    NOT NULL,
    is_renewal       bool default false,
    foreign key (account_id) references saving_account (account_id)
        on delete cascade
        on update cascade,
    foreign key (deposit_type) references fixed_deposit_type (deposit_type)
        on delete cascade
        on update cascade,
    check ( deposit_amount >= 0 )
);

create table transaction
(
    transaction_id      int primary key auto_increment,
    card_id             nchar(19)      NOT NULL,
    card_type           varchar(15)    NOT NULL,
    transaction_time    datetime       NOT NULL,
    transaction_amount  decimal(20, 2) NOT NULL,
    transaction_type    varchar(20)    NOT NULL,
    transaction_channel varchar(20) default NULL,
    currency            nchar(3)    default 'CNY',
    money_source        nchar(19)   default NULL,
    money_goes          nchar(19)   default NULL,
    check ( transaction_amount >= 0)
);

create table freeze_state_record
(
    freeze_state_record_id int primary key auto_increment,
    account_id             nchar(19)   NOT NULL,
    freeze_time            datetime    NOT NULL,
    unfreeze_time          datetime    NOT NULL,
    freeze_reason          varchar(50) NOT NULL,
    foreign key (account_id) references saving_account (account_id)
        on delete cascade
        on update cascade
);

create table loss_state_record
(
    loss_state_record_id int primary key auto_increment,
    account_id           nchar(19) NOT NULL,
    loss_time            datetime  NOT NULL,
    reissue_time         datetime DEFAULT NULl,
    foreign key (account_id) references saving_account (account_id)
        on delete cascade
        on update cascade
);

create table cashier
(
    cashier_id   int primary key auto_increment,
    cashier_name varchar(20)        NOT NULL,
    id_number    varchar(20) unique NOT NULL,
    phone_number varchar(20)        NOT NULL,
    password     varchar(20)        NOT NULL,
    address      varchar(50)        NOT NULL,
    privilege    char               NOT NULL,
    check ( privilege in ('A', 'B', 'C'))
) auto_increment = 100001;


# 定期存款 利率表 初始化
# 存款名称 持续月份 年利率
insert into fixed_deposit_type
values ('A', 3, 0.0115);
insert into fixed_deposit_type
values ('B', 6, 0.0135);
insert into fixed_deposit_type
values ('C', 12, 0.0145);
insert into fixed_deposit_type
values ('D', 24, 0.0165);
insert into fixed_deposit_type
values ('E', 36, 0.0195);
insert into fixed_deposit_type
values ('F', 60, 0.0200);


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

create table creditcards
(
    card_id         varchar(23)    not null,
    customer_id     int            not null,
    deposit_card_id varchar(19)    not null,
    password        varchar(255)   not null,
    status          int            not null,
    credit_limit    decimal(10, 2) not null,
    consumption     decimal(10, 2) not null default 0,
    primary key (card_id),
    foreign key (customer_id) references customer (customer_id)
        on delete cascade
        on update cascade,
    #   foreign key (deposit_card_id) references cards (card_id) on delete restrict on update cascade,
    check (credit_limit >= 0 and consumption >= 0 and consumption <= creditcards.credit_limit)
) engine = innodb
  charset = utf8mb4;

# 外汇
create table wh_currency
(
    currency_id   int            not null primary key,
    currency_name varchar(15)    not null,
    currency_rate decimal(15, 6) not null
);

Create table wh_account
(
    user_id      int         not null primary key,
    user_name    varchar(20) not null,
    password     varchar(20) not null,
    pay_password varchar(20) not null
);


Create table wh_user_currency
(
    user_id     int            not null,
    currency_id int            not null,
    amount      decimal(15, 2) not null,
    FOREIGN KEY (user_id) REFERENCES wh_account (user_id),
    FOREIGN KEY (currency_id) REFERENCES wh_currency (currency_id)
);


create table wh_transaction
(
    transaction_id   int            not null auto_increment primary key,
    user_id          int            not null,
    currency_to_id   int            not null,
    currency_from_id int            not null,
    amount_to        decimal(15, 2) not null,
    amount_from      decimal(15, 2) not null,
    transaction_time timestamp default current_timestamp,
    foreign key (user_id) references wh_account (user_id),
    foreign key (currency_to_id) references wh_currency (currency_id),
    foreign key (currency_from_id) references wh_currency (currency_id)
);

CREATE TABLE wh_operator
(
    opt_id           int         not null primary key,
    opt_name         varchar(50) not null,
    opt_password     VARCHAR(50) not null,
    control_currency boolean     not null default 0,
    control_rate     boolean     not null default 0
);