CREATE TABLE etf (
    symbol     CHAR(6) PRIMARY KEY,
    name       VARCHAR(200) NOT NULL,
    nav        DECIMAL(18, 2),
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_etf_name ON etf (name);

CREATE TABLE etf_sync_log (
    id          VARCHAR(36) PRIMARY KEY,
    started_at  TIMESTAMP NOT NULL,
    finished_at TIMESTAMP,
    row_count   INTEGER,
    status      VARCHAR(20) NOT NULL
);

CREATE TABLE price_history (
    symbol CHAR(6) NOT NULL,
    date   DATE NOT NULL,
    open   BIGINT NOT NULL,
    high   BIGINT NOT NULL,
    low    BIGINT NOT NULL,
    close  BIGINT NOT NULL,
    volume BIGINT NOT NULL,
    PRIMARY KEY (symbol, date)
);

CREATE INDEX idx_price_history_symbol_date ON price_history (symbol, date);
