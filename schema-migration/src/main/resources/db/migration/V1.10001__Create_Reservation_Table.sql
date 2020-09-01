CREATE TABLE IF NOT EXISTS reservation
(
     id         	UUID NOT NULL,
     user_id        VARCHAR(50) NOT NULL,
     check_in_date  DATE,
     check_out_date DATE,
     PRIMARY KEY (id)
)