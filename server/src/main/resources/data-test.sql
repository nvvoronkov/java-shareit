INSERT INTO users(NAME, EMAIL)
VALUES ('User1', 'User1@email.com'),
       ('User2', 'User2@email.com'),
       ('User3', 'User3@email.com'),
       ('User4', 'User4@email.com');

INSERT INTO requests(description, requester_id, created_date)
VALUES ('Request1 description', 1, '2022-01-01T10:10:10'),
       ('Request2 description', 1, '2022-02-01T10:10:10'),
       ('Request3 description', 1, '2022-03-01T10:10:10'),
       ('Request4 description', 2, '2022-04-01T10:10:10'),
       ('Request5 description', 2, '2022-05-01T10:10:10');

INSERT INTO items(NAME, DESCRIPTION, available, OWNER_ID)
VALUES ('Item1', 'Description1', true, 1),
       ('Item11', 'Description11 for search', false, 1),
       ('Item2', 'Description2', true, 2),
       ('Item22', 'Description22 for SEARCH', true, 2),
       ('Item3', 'Description3', true, 3),
       ('Item33', 'Description33 for SeaRcH', true, 3);

INSERT INTO items(NAME, DESCRIPTION, available, OWNER_ID, REQUEST_ID)
VALUES ('Item4 forsearch', 'Description4', true, 4, 1),
       ('Item44', 'Description44', true, 4, 1);

INSERT INTO bookings(start_date, end_date, item_id, booker_id, status)
VALUES ('2021-01-09T10:10:10', '2021-06-01T10:10:10', 3, 1, 0),
       ('2021-07-01T10:10:10', '2021-12-01T10:10:10', 1, 2, 0),
       ('2022-01-01T10:10:10', '2022-12-30T10:10:10', 3, 1, 0),
       ('2022-01-01T10:10:10', '2022-12-30T10:10:10', 1, 2, 0),
       ('2023-01-01T10:10:10', '2023-06-01T10:10:10', 3, 1, 2),
       ('2023-07-01T10:10:10', '2023-12-01T10:10:10', 1, 2, 2),
       ('2021-01-09T10:10:10', '2021-06-01T10:10:10', 3, 1, 2),
       ('2021-01-01T10:10:10', '2021-12-01T10:10:10', 2, 4, 1);

INSERT INTO comments(text, item_id, author_id, created_date)
VALUES ('Comment text1', 1, 2, '2022-01-01T10:10:10'),
       ('Comment text2', 1, 2, '2022-02-01T10:10:10'),
       ('Comment text3', 3, 3, '2022-01-01T10:10:10'),
       ('Comment text4', 3, 3, '2022-02-01T10:10:10');