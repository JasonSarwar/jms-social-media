CREATE TABLE IF NOT EXISTS user_sessions (
	user_id INT NOT NULL,
	session_key VARCHAR(36) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    PRIMARY KEY (session_key),
    CONSTRAINT fk_user_sessions_user_id FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);