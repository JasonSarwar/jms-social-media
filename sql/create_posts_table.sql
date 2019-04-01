CREATE TABLE IF NOT EXISTS posts (
	post_id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    text VARCHAR(512) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    PRIMARY KEY (post_id),
    CONSTRAINT fk_posts_users_user_id FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);