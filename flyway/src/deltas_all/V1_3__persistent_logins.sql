CREATE TABLE thatpersistentlogins (
    TPL_USERNAME varchar(255) not null,
    TPL_SERIES varchar(255) not null,
    TPL_TOKEN varchar(255) not null,
    TPL_LAST_USED timestamp not null,
    PRIMARY KEY (TPL_SERIES)
);