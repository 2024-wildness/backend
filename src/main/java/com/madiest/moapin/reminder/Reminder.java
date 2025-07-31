package com.madiest.moapin.reminder;

import com.madiest.moapin.auth.User;
import com.madiest.moapin.content.Content;

import javax.persistence.*;
import java.time.Instant;

/**
 * Reminder entity storing scheduled notifications for content.
 */
@Entity
@Table(name = "reminder")
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "content_id")
    private Content content;

    @Column(name = "remind_at", nullable = false)
    private Instant remindAt;

    @Column(nullable = false)
    private boolean processed = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Instant getRemindAt() {
        return remindAt;
    }

    public void setRemindAt(Instant remindAt) {
        this.remindAt = remindAt;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
