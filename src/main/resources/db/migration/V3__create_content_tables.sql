-- Create single-table inheritance storage for Content and its subtypes (LINK, NOTE, PHOTO)
CREATE TABLE IF NOT EXISTS content (
    id BIGSERIAL PRIMARY KEY,
    content_type VARCHAR(16) NOT NULL,
    title VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    is_pinned BOOLEAN NOT NULL DEFAULT FALSE,
    view_count INT NOT NULL DEFAULT 0,
    -- Subtype columns (nullable)
    url VARCHAR(2000) NULL,
    body TEXT NULL,
    file_key VARCHAR(512) NULL,
    -- Auditing
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_content_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_content_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_content_user ON content(user_id);
CREATE INDEX IF NOT EXISTS idx_content_category ON content(category_id);
CREATE INDEX IF NOT EXISTS idx_content_category_user ON content(category_id, user_id);
CREATE INDEX IF NOT EXISTS idx_content_type ON content(content_type);

