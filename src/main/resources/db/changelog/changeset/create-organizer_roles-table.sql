CREATE TABLE IF NOT EXISTS organizer_roles
(
    role_id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name       VARCHAR(50),
    CONSTRAINT pk_org_role PRIMARY KEY (role_id)
    );