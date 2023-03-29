CREATE TABLE users
(
    username          VARCHAR(255) NOT NULL,
    email             VARCHAR(255),
    password          VARCHAR(255),
    balance           INT          NOT NULL,
    locked_balance    INT          NOT NULL,
    courier_status_id UUID,
    enabled           BOOLEAN      NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (username)
);

CREATE TABLE user_roles
(
    username VARCHAR(255) NOT NULL,
    roles    VARCHAR(255)
);

CREATE TABLE courier_status
(
    id        UUID NOT NULL,
    status    VARCHAR(255),
    latitude  VARCHAR(255),
    longitude VARCHAR(255),
    CONSTRAINT pk_courier_status PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_COURIER_STATUS FOREIGN KEY (courier_status_id) REFERENCES courier_status (id);

ALTER TABLE user_roles
    ADD CONSTRAINT FK_USER_ROLES_ON_USER FOREIGN KEY (username) REFERENCES users (username);

CREATE INDEX idx_user_roles_username ON user_roles (username);

CREATE INDEX idx_courier_status_status ON courier_status (status);