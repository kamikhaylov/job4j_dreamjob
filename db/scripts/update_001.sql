CREATE TABLE post (
    id          SERIAL PRIMARY KEY,
    name        TEXT,
    description TEXT,
    created     TIMESTAMP,
    visible     BOOLEAN DEFAULT FALSE,
    city_id     INT
);

CREATE TABLE candidate (
    id          SERIAL PRIMARY KEY,
    name        TEXT,
    description TEXT,
    created     TIMESTAMP,
    visible     BOOLEAN DEFAULT FALSE,
    city_id     INT,
    photo       TEXT
);

CREATE TABLE users (
    id         SERIAL PRIMARY KEY,
    email      VARCHAR(255),
    password   TEXT,
    name       TEXT
);

ALTER TABLE users ADD CONSTRAINT email_unique UNIQUE (email);