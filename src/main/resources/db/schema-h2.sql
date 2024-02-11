CREATE TABLE BPI
(
    ID          INT auto_increment PRIMARY KEY,
    NAME        VARCHAR(50),
    CODE        VARCHAR(50),
    SYMBOL      VARCHAR(50),
    RATE        VARCHAR(50),
    DESCRIPTION VARCHAR(255),
    RATE_FLOAT  NUMERIC(20, 2)
);

INSERT INTO BPI VALUES ('美元', 'USD', '&#36;','45,088.402','United States Dollar', 45088.40);
INSERT INTO BPI VALUES ('英鎊', 'GBP', '&pound;','35,840.185','British Pound Sterling', 41967.25);