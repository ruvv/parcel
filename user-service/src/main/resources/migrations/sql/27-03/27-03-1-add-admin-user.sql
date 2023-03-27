INSERT INTO courier_status (id, status, latitude, longitude)
VALUES ('6e80121a-72e8-4de6-9d20-9a8d5b5c03be', 'OFF_DUTY', null, null);

INSERT INTO users (username, email, courier_status_id, password, balance, enabled)
VALUES ('admin', 'admin@yahoo.com', '6e80121a-72e8-4de6-9d20-9a8d5b5c03be',
        '$2a$11$QkBlD60tVZBVli9I6UzgY.LnH5tIK90D1KabLv3cW53ydTaj0svPK', 0, true);

INSERT INTO user_roles(username, roles)
VALUES ('admin', 'ADMIN');