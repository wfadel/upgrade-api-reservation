CREATE TABLE IF NOT EXISTS users
(
     id             SERIAL,
     email          VARCHAR(50) UNIQUE NOT NULL,
     first_name     VARCHAR(20) NOT NULL,
     last_name      VARCHAR(20) NOT NULL,
     PRIMARY KEY (id)
)