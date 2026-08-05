ALTER TABLE etf ADD COLUMN tab_code   INTEGER;
ALTER TABLE etf ADD COLUMN market_sum BIGINT;
ALTER TABLE etf ADD COLUMN quant      BIGINT;

CREATE INDEX idx_etf_tab_code ON etf (tab_code);
CREATE INDEX idx_etf_market_sum ON etf (market_sum);
