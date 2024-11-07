--liquibase formatted sql

--changeset penkin:role

CREATE TABLE role
(
    id   UUID primary key,
    name text not null
)