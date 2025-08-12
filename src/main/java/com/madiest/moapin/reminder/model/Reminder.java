package com.madiest.moapin.reminder.model;

import com.madiest.moapin.auth.model.User;
import com.madiest.moapin.content.model.Content;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Instant remindAt;

    private boolean processed;

    public Reminder(Content content, User user, Instant remindAt) {
        this.content = content;
        this.user = user;
        this.remindAt = remindAt;
        this.processed = false;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
