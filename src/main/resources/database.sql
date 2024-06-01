create database mybank;
use mybank;

create table customer(
                         customer_id int primary key auto_increment,
                         customer_name varchar(20) NOT NULL,
                         id_number varchar(20) NOT NULL,
                         phone_number varchar(15) NOT NULL,
                         address varchar(50) NOT NULL,
                         credit_line decimal(15,2) default 0,
                         assets decimal(20,2) default 0,
                         internet_bank_open boolean default false,
                         check (credit_line >= 0),
                         check (assets >=0 )
);

create table saving_account(
                               account_id nchar(19) primary key,
                               customer_id int NOT NULL,
                               password varchar(20) NOT NULL,
                               balance decimal(20,2) NOT NULL,
                               state varchar(10) NOT NULL,
                               open_time datetime NOT NULL,
                               open_amount decimal(15,2) NOT NULL,
                               foreign key (customer_id) references customer(customer_id)
                                   on delete cascade
                                   on update cascade,
                               check ( balance>=0 )
);

create table cashier(
                        cashier_id int primary key,
                        cashier_name varchar(20) NOT NULL,
                        id_number varchar(20) NOT NULL,
                        phone_number varchar(15) NOT NULL,
                        password varchar(20) NOT NULL,
                        privilege char NOT NULL
                            check ( privilege in ('A', 'B', 'C'))
);

create table fixed_deposit(
                              fixed_deposit_id int primary key,
                              account_id int NOT NULL,
                              deposit_time long NOT NULL,
                              deposit_amount decimal(15,2) NOT NULL,
                              deposit_type varchar(10) NOT NULL,
                              foreign key (account_id) references account(account_id)
                                  on delete cascade
                                  on update cascade,
                              check ( deposit_amount >= 0 )
);

create table transaction(
                            transaction_id int primary key,
                            card_id nchar(19) NOT NULL ,
                            card_type varchar(5) NOT NULL ,
                            transaction_time datetime NOT NULL,
                            transaction_amount decimal(20,2) NOT NULL,
                            transaction_type varchar(20) NOT NULL,
                            transaction_channel varchar(10) NOT NULL ,
                            currency varchar(50) NOT NULL ,
                            money_source nchar(19) ,
                            money_goes  nchar(19),

                            check ( transaction_type in ('deposit','withdraw','transfer') ),
                            check ( transaction_amount>= 0)
);

create table state_record(
                             state_record_id int primary key,
                             account_id int NOT NULL,
                             freeze_time long NOT NULL,
                             unfreeze_time long NOT NULL,
                             freeze_reason varchar(50) NOT NULL,
                             foreign key (account_id) references account(account_id)
                                 on delete cascade
                                 on update cascade
);