CREATE TABLE IF NOT EXISTS users (
	user_id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(16) NOT NULL UNIQUE,
    hashed_password BINARY(60) NOT NULL,
    full_name VARCHAR(64) NOT NULL,
    email VARCHAR(64) NOT NULL UNIQUE,
    bio VARCHAR(512),
    birth_date DATE,
    date_time_joined TIMESTAMP NOT NULL,
    profile_picture_link VARCHAR(256),
    time_of_last_activity TIMESTAMP,
    PRIMARY KEY (user_id)
);