--liquibase formatted sql

--changeset saikiran.pv:1
create table sites(
    id bigint(20) unsigned not null AUTO_INCREMENT,
    site_name varchar(150) not null,
    address varchar(1024) default null,
    current_status enum('YET_TO_START', 'IN_PROGRESS', 'SUSPENDED', 'COMPLETED'),
    supervisors varchar(2048) default null,
    vehicles varchar(2048) default null,
    work_start timestamp NULL DEFAULT NULL,
    work_end timestamp NULL DEFAULT NULL,
    created_by varchar(255) DEFAULT NULL,
    updated_by varchar(255) DEFAULT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    primary key (id)
)ENGINE=InnoDB default CHARSET=utf8;