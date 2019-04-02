CREATE TABLE IF NOT EXISTS followers (
	follower_user_id INT NOT NULL,
	following_user_id INT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    PRIMARY KEY (follower_user_id, following_user_id),
    CONSTRAINT fk_follower_user_id FOREIGN KEY (follower_user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_following_user_id FOREIGN KEY (following_user_id) REFERENCES users(user_id) ON DELETE CASCADE
);