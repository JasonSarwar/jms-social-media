CREATE TABLE IF NOT EXISTS comment_likes (
	comment_id INT NOT NULL,
	user_id INT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    PRIMARY KEY (comment_id, user_id),
    CONSTRAINT fk_comment_likes_comment_id FOREIGN KEY (comment_id) REFERENCES comments(comment_id) ON DELETE CASCADE,
	CONSTRAINT fk_comment_likes_user_id FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);