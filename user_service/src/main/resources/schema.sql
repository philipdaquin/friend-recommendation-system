CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(254) UNIQUE,
    created_date DATE ,
    created_by VARCHAR(50),
    last_modified_by VARCHAR(50),
    last_modified_date DATE 
);