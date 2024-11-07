--liquibase formatted sql

-- changeset penkin:admin-data

INSERT INTO users(id, username, password) VALUES ('536335ca-d4f4-4039-830f-f83876f6394d', 'admin', '$2a$10$alO8WKtzJXHJBl3U/XNdUeeTyoLWn9qHpdAnIOAtYKlNbnbhakfWa');
INSERT INTO user_roles(user_id, role_id) VALUES ('536335ca-d4f4-4039-830f-f83876f6394d', '8b5d9d9e-9274-4893-bebd-d2e6f4dc0f7b');