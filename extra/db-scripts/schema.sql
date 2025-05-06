CREATE SCHEMA IF NOT EXISTS bank_account;

CREATE TABLE IF NOT EXISTS bank_account.bank_user (
    id UUID PRIMARY KEY,
    username varchar(255) NOT NULL UNIQUE,
    password text NOT NULL,
    roles varchar[]
);