CREATE TABLE post (
    id          SERIAL PRIMARY KEY,
    name        TEXT,
    description TEXT,
    created     TIMESTAMP,
    visible     BOOLEAN DEFAULT FALSE,
    city_id     INT
);