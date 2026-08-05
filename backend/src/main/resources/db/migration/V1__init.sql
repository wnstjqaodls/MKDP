CREATE TABLE company (
    corp_code   CHAR(8) PRIMARY KEY,
    corp_name   VARCHAR(200) NOT NULL,
    stock_code  CHAR(6),
    modify_date CHAR(8),
    updated_at  TIMESTAMP NOT NULL
);

CREATE INDEX idx_company_name ON company (corp_name);
CREATE INDEX idx_company_stock ON company (stock_code);

CREATE TABLE corp_code_sync_log (
    id          VARCHAR(36) PRIMARY KEY,
    started_at  TIMESTAMP NOT NULL,
    finished_at TIMESTAMP,
    row_count   INTEGER,
    status      VARCHAR(20) NOT NULL
);
