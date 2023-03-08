--liquibase formatted sql

--changeset saikiran.pv:1
CREATE TABLE users(
    name varchar(50) not null,
    mobile_num bigint(20) not null,
    admin boolean default false not null,
    PRIMARY KEY (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;