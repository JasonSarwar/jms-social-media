CREATE TABLE IF NOT EXISTS comment_mentions (
	comment_id INT NOT NULL,
	user_id INT NOT NULL,
    PRIMARY KEY (comment_id, user_id),
    CONSTRAINT fk_comment_mentions_comment_id FOREIGN KEY (comment_id) REFERENCES comments(comment_id) ON DELETE CASCADE,
	CONSTRAINT fk_comment_mentions_user_id FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);