CREATE TABLE IF NOT EXISTS post_likes (
	post_id INT NOT NULL,
	user_id INT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    PRIMARY KEY (post_id, user_id),
    CONSTRAINT fk_post_likes_post_id FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
	CONSTRAINT fk_post_likes_user_id FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);