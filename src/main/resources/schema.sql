DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS requests;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email varchar(320),
    name  varchar(100),
    CONSTRAINT unique_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description  varchar(1000),
    requester_id BIGINT,
    CONSTRAINT fk_requests_to_users FOREIGN KEY (requester_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS items
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        varchar(100),
    description varchar(1000),
    available   BOOLEAN,
    owner_id    BIGINT,
    request_id  BIGINT,
    CONSTRAINT fk_items_to_users FOREIGN KEY (owner_id) REFERENCES users (id),
    UNIQUE (id, owner_id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date   TIMESTAMP WITHOUT TIME ZONE,
    item_id    BIGINT,
    booker_id  BIGINT,
    status     varchar(20),
    CONSTRAINT fk_bookings_to_items FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_bookings_to_users FOREIGN KEY (booker_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text         varchar(2000),
    item_id      BIGINT,
    author_id    BIGINT,
    created_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_comments_to_items FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_comments_to_users FOREIGN KEY (author_id) REFERENCES users (id)
);
