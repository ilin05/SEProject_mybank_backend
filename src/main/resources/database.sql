create database mybank;
use mybank;

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