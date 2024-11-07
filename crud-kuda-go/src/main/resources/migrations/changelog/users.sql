--liquibase formatted sql

--changeset penkin:users

CREATE TABLE users
(
    id       UUID primary key,
    username text not null,
    password text not null
);