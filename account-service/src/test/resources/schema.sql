CREATE SCHEMA IF NOT EXISTS bank_account;

CREATE TABLE IF NOT EXISTS bank_account.bank_user (
    id UUID PRIMARY KEY,
    username varchar(255) NOT NULL UNIQUE,
    password text NOT NULL,
    birthday date,
    full_name varchar(255),
    email varchar(255),
    roles varchar array
);

CREATE TABLE IF NOT EXISTS bank_account.user_account (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES bank_account.bank_user(id),
    currency varchar(50) NOT NULL,
    balance NUMERIC NOT NULL
)