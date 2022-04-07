
CREATE TABLE IF NOT EXISTS department
(
    id     SERIAL PRIMARY KEY,
    name   VARCHAR(80) UNIQUE,
    closed BOOLEAN DEFAULT FALSE NOT NULL
    );

CREATE TABLE IF NOT EXISTS person
(
    id             SERIAL PRIMARY KEY,
    first_name     VARCHAR(80) NOT NULL,
    last_name      VARCHAR(80) NOT NULL,
    middle_name    VARCHAR(80),
    age            INTEGER,
    department_id  INTEGER
);

alter table person
    add constraint department__fk
        foreign key (department_id) references department (id);