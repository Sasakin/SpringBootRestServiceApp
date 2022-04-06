CREATE TABLE IF NOT EXISTS person
(
    id             INTEGER NOT NULL CONSTRAINT person_pkey PRIMARY KEY,
    first_name     VARCHAR(80) NOT NULL,
    last_name      VARCHAR(80) NOT NULL,
    middle_name    VARCHAR(80),
    age            INTEGER,
    department_id INTEGER
    );