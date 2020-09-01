CREATE TABLE IF NOT EXISTS availability
(
     id             SERIAL,
     day            DATE NOT NULL UNIQUE,
     reservation_id VARCHAR(50),
     version        BIGINT DEFAULT 0,
     PRIMARY KEY(id)
)