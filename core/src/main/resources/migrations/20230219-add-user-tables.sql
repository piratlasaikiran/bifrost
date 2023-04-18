--liquibase formatted sql

--changeset saikiran.pv:1
create table supervisors(
    id bigint(20) unsigned not null AUTO_INCREMENT,
	name varchar(150) not null,
	personal_mobile_num bigint(10) not null,
	bank_ac varchar(150) not null,
	salary integer,
	admin tinyint(1) not null default 0,
	company_mob_num bigint(10) default null,
	atm_card bigint(30) default null,
	vehicle_num varchar(25) default null,
	ot_pay integer not null default 0,
	created_by varchar(255) DEFAULT NULL,
	updated_by varchar(255) DEFAULT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
)ENGINE=InnoDB default CHARSET=utf8;

create table drivers(
    id bigint(20) unsigned not null AUTO_INCREMENT,
	name varchar(150) not null,
	personal_mobile_num bigint(10) not null,
	bank_ac varchar(150) not null,
	salary integer,
	admin tinyint(1) not null default 0,
	ot_pay_day integer not null default 0,
	ot_pay_day_night integer not null default 0,
	license longblob DEFAULT NULL,
	aadhar longblob DEFAULT NULL,
	created_by varchar(255) DEFAULT NULL,
	updated_by varchar(255) DEFAULT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
)ENGINE=InnoDB default CHARSET=utf8;