CREATE TABLE IF NOT EXISTS account_summary
(
    id              INT            NOT NULL AUTO_INCREMENT,
    account_number  VARCHAR(10)    NOT NULL,
    current_balance DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (id)
)
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS hibernate_customer
(
    id            BIGINT      NOT NULL PRIMARY KEY,
    firstName     VARCHAR(11) NOT NULL,
    middleInitial VARCHAR(1),
    lastName      VARCHAR(10) NOT NULL,
    address       VARCHAR(45) NOT NULL,
    city          VARCHAR(16) NOT NULL,
    state         CHAR(2)     NOT NULL,
    zipCode       CHAR(5)
);

CREATE TABLE CUSTOMER
(
    id            BIGINT      NOT NULL PRIMARY KEY,
    firstName     VARCHAR(11) NOT NULL,
    middleInitial VARCHAR(1),
    lastName      VARCHAR(10) NOT NULL,
    address       VARCHAR(45) NOT NULL,
    city          VARCHAR(16) NOT NULL,
    state         CHAR(2)     NOT NULL,
    zipCode       CHAR(5)
);

CREATE TABLE IWCUSTOMER
(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(45) ,
    middle_initial VARCHAR(1) ,
    last_name VARCHAR(45) ,
    address VARCHAR(45) ,
    city VARCHAR(45) ,
    state VARCHAR(2) ,
    zip VARCHAR(5),
    email VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS transaction
(
    id                 INT           NOT NULL AUTO_INCREMENT,
    timestamp          TIMESTAMP     NOT NULL,
    amount             DECIMAL(8, 2) NOT NULL,
    account_summary_id INT,
    PRIMARY KEY (id),
    INDEX fk_Transaction_Account_Summary (account_summary_id ASC),
    CONSTRAINT fk_Transaction_Account_Summary
        FOREIGN KEY (account_summary_id)
            REFERENCES Account_Summary (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;