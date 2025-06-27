INSERT INTO accounts
(aid, email, username, password, created_at, updated_at, role, status)
VALUES (1, 'admin@ngow.com', 'admin', 'Admin@1', '2024-12-20 11:44:54.0',
        '2024-12-20 11:44:54.0', 'ADMIN', 'ACTIVE'),
       (2, 'ngow@gmail.com', 'ngow', '12345', '2024-12-20 11:44:54.0', '2024-12-20 11:44:54.0',
        'USER', 'ACTIVE');

SELECT setval('accounts_aid_seq', (SELECT MAX(aid) FROM accounts));
