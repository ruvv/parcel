CREATE TABLE parcels
(
    id          BIGINT NOT NULL,
    status      VARCHAR(255),
    source      VARCHAR(255),
    destination VARCHAR(255),
    description VARCHAR(255),
    cost        INT NOT NULL,
    created_by  VARCHAR(255),
    assigned_to VARCHAR(255),
    created_at  TIMESTAMP WITH TIME ZONE,
    updated_at  TIMESTAMP WITH TIME ZONE,
    CONSTRAINT pk_parcels PRIMARY KEY (id)
);

CREATE INDEX idx_parcels_created_by ON parcels (created_by);

CREATE INDEX idx_parcels_assigned_to ON parcels (assigned_to);

CREATE SEQUENCE PARCEL_ID_SEQUENCE START WITH 1 INCREMENT BY 16;