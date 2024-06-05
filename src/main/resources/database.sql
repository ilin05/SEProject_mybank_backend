create database mybank;
use mybank;

create table customer
(
    customer_id        int primary key auto_increment,
    customer_name      varchar(20)        NOT NULL,
    id_number          varchar(20) unique NOT NULL,
    phone_number       varchar(15)        NOT NULL,
    address            varchar(50)        NOT NULL,
    credit_line        decimal(15, 2) default 0,
    assets             decimal(20, 2) default 0,
    internet_bank_open boolean        default false,
    check (credit_line >= 0),
    check (assets >= 0 )
);

create table saving_account
(
<<<<<<< HEAD
    account_id  nchar(19) primary key,
    customer_id int            NOT NULL,
    password    varchar(20)    NOT NULL,
    balance     decimal(20, 2) NOT NULL,
    freeze_state BOOL    DEFAULT false,
    loss_state   BOOL    DEFAULT false,
    deleted      BOOL    DEFAULT false,
    open_time   datetime       NOT NULL,
    open_amount decimal(15, 2) NOT NULL,
=======
    account_id   nchar(19) primary key,
    customer_id  int            NOT NULL,
    password     varchar(20)    NOT NULL,
    balance      decimal(20, 2) NOT NULL,
    freeze_state BOOL DEFAULT false,
    loss_state   BOOL DEFAULT false,
    deleted      BOOL DEFAULT false,
    open_time    datetime       NOT NULL,
    open_amount  decimal(15, 2) NOT NULL,
>>>>>>> 6deb432691123f82f765cca5aaac96bc6c102985
    foreign key (customer_id) references customer (customer_id)
        on delete cascade
        on update cascade,
    check ( balance >= 0 )
);

create table cashier
(
    cashier_id   int primary key auto_increment,
    cashier_name varchar(20)        NOT NULL,
    id_number    varchar(20) unique NOT NULL,
    phone_number varchar(15)        NOT NULL,
    password     varchar(20)        NOT NULL,
    address      varchar(50)        NOT NULL,
    privilege    char               NOT NULL
        check ( privilege in ('A', 'B', 'C'))
) auto_increment = 100001;

create table fixed_deposit
(
    fixed_deposit_id int primary key auto_increment,
    account_id       nchar(19)      NOT NULL,
    deposit_time     datetime       NOT NULL,
    deposit_amount   decimal(15, 2) NOT NULL,
    deposit_type     varchar(10)    NOT NULL,
    foreign key (account_id) references saving_account (account_id)
        on delete cascade
        on update cascade,
    check ( deposit_amount >= 0 )
);

create table transaction
(
    transaction_id      int primary key auto_increment,
    card_id             nchar(19)      NOT NULL,
    card_type           varchar(5)     NOT NULL,
    transaction_time    datetime       NOT NULL,
    transaction_amount  decimal(20, 2) NOT NULL,
    transaction_type    varchar(20)    NOT NULL,
    transaction_channel varchar(10)    NOT NULL,
    currency            nchar(3)       NOT NULL,
    money_source        nchar(19),
    money_goes          nchar(19),
    check ( transaction_amount >= 0)
);

create table freeze_state_record
(
    freeze_state_record_id int primary key auto_increment,
<<<<<<< HEAD
    account_id      nchar(19)   NOT NULL,
    freeze_time     datetime    NOT NULL,
    unfreeze_time   datetime    NOT NULL,
    freeze_reason   varchar(50) NOT NULL,
=======
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
>>>>>>> 6deb432691123f82f765cca5aaac96bc6c102985
    foreign key (account_id) references saving_account (account_id)
        on delete cascade
        on update cascade
);
create table loss_state_record
(
    loss_state_record_id int primary key auto_increment,
    account_id      nchar(19)   NOT NULL,
    loss_time     datetime    NOT NULL,
    reissue_time  datetime    DEFAULT NULl,
    foreign key (account_id) references saving_account (account_id)
        on delete cascade
        on update cascade
);