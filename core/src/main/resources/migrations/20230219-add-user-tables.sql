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
	aadhar longblob DEFAULT NULL,
	created_by varchar(255) DEFAULT NULL,
	updated_by varchar(255) DEFAULT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    primary key (id)
)ENGINE=InnoDB default CHARSET=utf8;

--changeset saikiran.pv:2
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
    primary key (id)
)ENGINE=InnoDB default CHARSET=utf8;

--changeset saikiran.pv:3
create table vehicles(
    vehicle_num varchar(25) not null,
    owner varchar(150) not null,
    chassis_num varchar(100) not null,
    engine_num varchar(100) not null,
    vehicle_class varchar(100) not null,
    insurance_provider varchar(100) not null,
    finance_provider varchar(100) not null,
    created_by varchar(255) DEFAULT NULL,
    updated_by varchar(255) DEFAULT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    primary key (vehicle_num)
)ENGINE=InnoDB default CHARSET=utf8;

--changeset saikiran.pv:4
create table vehicle_taxes(
    id bigint(20) unsigned not null AUTO_INCREMENT,
    vehicle_num varchar(25) not null,
    tax_type enum('OTHERS', 'PUC', 'FITNESS', 'PERMIT', 'INSURANCE', 'TAX'),
    tax_amount int(10) not null,
    tax_doc longblob default null,
    validity_start timestamp NULL DEFAULT NULL,
    validity_end timestamp NULL DEFAULT NULL,
    created_by varchar(255) default null,
    updated_by varchar(255) default null,
    created_at timestamp not null default CURRENT_TIMESTAMP,
    updated_at timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    primary key(id),
    constraint vehicle_taxes_vehicle_num_fk FOREIGN KEY(vehicle_num) REFERENCES vehicles (vehicle_num)
)ENGINE=InnoDB default CHARSET=utf8;
