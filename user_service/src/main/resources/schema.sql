CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(254) UNIQUE,
    created_date TIMESTAMP DEFAULT NOW(),
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50) NULL,
    last_modified_date TIMESTAMP NULL
);

CREATE TYPE FriendRequestStatus AS ENUM (
    'PENDING',
    'ACCEPTED',
    'REJECTED',
    'CANCELED'
);

CREATE TABLE IF NOT EXISTS friend_requests (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    friend_id BIGINT NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    request_status FriendRequestStatus NOT NULL DEFAULT 'PENDING'
);