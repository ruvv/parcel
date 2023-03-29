INSERT INTO users (username, email, courier_status_id, password, balance, locked_balance, enabled)
VALUES ('admin', 'admin@yahoo.com', null,
        '$2a$11$QkBlD60tVZBVli9I6UzgY.LnH5tIK90D1KabLv3cW53ydTaj0svPK', 0, 0, true);

INSERT INTO user_roles(username, roles)
VALUES ('admin', 'ADMIN');