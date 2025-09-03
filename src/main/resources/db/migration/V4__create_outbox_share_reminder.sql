-- Outbox events for search indexing
CREATE TABLE IF NOT EXISTS outbox_event (
    id BIGSERIAL PRIMARY KEY,
    content_id BIGINT NOT NULL,
    operation VARCHAR(16) NOT NULL,
    payload TEXT NULL,
    processed BOOLEAN NOT NULL DEFAULT FALSE
);
CREATE INDEX IF NOT EXISTS idx_outbox_content ON outbox_event(content_id);
CREATE INDEX IF NOT EXISTS idx_outbox_processed ON outbox_event(processed);

-- Share links for contents
CREATE TABLE IF NOT EXISTS share_link (
    id VARCHAR(64) PRIMARY KEY,
    content_id BIGINT NOT NULL,
    token VARCHAR(128) NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NULL,
    max_downloads INT NULL,
    download_count INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_share_content FOREIGN KEY (content_id) REFERENCES content (id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_share_content ON share_link(content_id);
CREATE INDEX IF NOT EXISTS idx_share_token ON share_link(token);

-- Reminders for users on contents
CREATE TABLE IF NOT EXISTS reminder (
    id BIGSERIAL PRIMARY KEY,
    content_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    remind_at TIMESTAMP WITH TIME ZONE NOT NULL,
    processed BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_reminder_content FOREIGN KEY (content_id) REFERENCES content (id) ON DELETE CASCADE,
    CONSTRAINT fk_reminder_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_reminder_user ON reminder(user_id);
CREATE INDEX IF NOT EXISTS idx_reminder_when ON reminder(remind_at);
