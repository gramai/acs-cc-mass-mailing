create table bank_users
(
    id               VARCHAR(36)  NOT NULL,
    email            VARCHAR(255) NOT NULL UNIQUE,
    iban             VARCHAR(36)  NOT NULL UNIQUE,
    available_amount VARCHAR(36),
    created_on       TIMESTAMP    NOT NULL,
    last_updated_on  TIMESTAMP,
    PRIMARY KEY (id)
);
