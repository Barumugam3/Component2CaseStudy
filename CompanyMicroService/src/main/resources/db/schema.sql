CREATE TABLE company (
company_code bigint NOT NULL,
companyceo VARCHAR(255),
company_name VARCHAR(255),
company_turn_over DECIMAL(19,2),
company_website VARCHAR(255),
latest_stock_price DECIMAL(19,2),
stock_exchange VARCHAR(255),
PRIMARY KEY (company_code)
);

