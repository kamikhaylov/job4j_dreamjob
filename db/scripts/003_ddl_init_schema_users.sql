CREATE TABLE users (
    id         SERIAL PRIMARY KEY,
    email      VARCHAR(255),
    password   TEXT,
    name       TEXT
);

ALTER TABLE users ADD CONSTRAINT email_unique UNIQUE (email);