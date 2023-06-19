--liquibase formatted sql

--changeset saikiran.pv:1
create table vendors(
	vendor_id varchar(256) not null,
	contract_doc longblob default null,
	location varchar(150) not null,
	mobile_number bigint(10) not default null,
	purpose varchar(150) default null,
	commodity_costs JSON default null,
	created_by varchar(255) default null,
	updated_by varchar(255) default null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp on update current_timestamp,
    primary key (vendor_id),
    constraint vendors_location_fk foreign key (location) references sites (site_name)
)ENGINE=InnoDB default CHARSET=utf8;

--changeset saikiran.pv:2
create table bank_accounts(
	nick_name varchar(256) not null,
	account_number varchar(256) not null,
	account_holders varchar(512) not null,
	bank_name varchar(256) not null,
    current_balance bigint(30) default null,
    atm_card bigint(30) default null,
	created_by varchar(255) default null,
	updated_by varchar(255) default null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp on update current_timestamp,
    primary key (nick_name)
)ENGINE=InnoDB default CHARSET=utf8;

--changeset saikiran.pv:3
create table transactions(
    id bigint(20) unsigned not null AUTO_INCREMENT,
	source varchar(256) not null,
	destination varchar(256) not null,
    amount bigint(30) default null,
    purpose varchar(255) default null,
    receipt longblob default null,
    remarks varchar(1024) default null,
    status varchar(255) default 'SUBMITTED',
    mode varchar(255) default 'CASH',
    transaction_date timestamp not null,
	created_by varchar(255) default null,
	updated_by varchar(255) default null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp on update current_timestamp,
    primary key (id)
)ENGINE=InnoDB default CHARSET=utf8;