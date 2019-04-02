CREATE TABLE IF NOT EXISTS comments (
	comment_id INT NOT NULL AUTO_INCREMENT,
	post_id INT NOT NULL,
    user_id INT NOT NULL,
    text VARCHAR(512) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    PRIMARY KEY (comment_id),
    CONSTRAINT fk_comments_users_user_id FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_posts_post_id FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE
);