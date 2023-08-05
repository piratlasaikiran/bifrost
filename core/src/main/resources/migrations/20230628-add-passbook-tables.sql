--liquibase formatted sql

--changeset saikiran.pv:1
create table passbook(
    id bigint(20) unsigned not null AUTO_INCREMENT,
	account_name varchar(256) not null,
	account_type varchar(150) not null,
	transaction_id bigint(20) unsigned default null,
	current_balance bigint(10) not null,
	transaction_amount bigint(10) not null,
	transaction_type varchar(150) not null,
	created_by varchar(255) default null,
	updated_by varchar(255) default null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp on update current_timestamp,
    primary key (id),
    foreign key (transaction_id) references transactions (id)
)ENGINE=InnoDB default CHARSET=utf8;

--changeset saikiran.pv:2
create table pending_balance(
    id bigint(20) unsigned not null AUTO_INCREMENT,
	account_name varchar(256) not null,
	transaction_id bigint(20) unsigned default null,
	pending_balance bigint(10) not null,
	created_by varchar(255) default null,
	updated_by varchar(255) default null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp on update current_timestamp,
    primary key (id),
    foreign key (transaction_id) references transactions (id)
)ENGINE=InnoDB default CHARSET=utf8;