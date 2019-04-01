CREATE TABLE IF NOT EXISTS post_tags (
	post_id INT NOT NULL,
	tag VARCHAR(64) NOT NULL,
    PRIMARY KEY (post_id, tag),
    CONSTRAINT fk_post_tags_post_id FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE
);