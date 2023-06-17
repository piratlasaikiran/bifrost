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