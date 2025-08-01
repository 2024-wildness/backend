package com.madiest.moapin.reminder;

import com.madiest.moapin.auth.User;
import com.madiest.moapin.content.Content;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Reminder entity storing scheduled notifications for content.
 */
@Entity
@Table(name = "reminder")
public class Reminder {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @Getter
    @ManyToOne(optional = false)
    @JoinColumn(name = "content_id")
    private Content content;

    @Setter
    @Getter
    @Column(name = "remind_at", nullable = false)
    private Instant remindAt;

    @Setter
    @Getter
    @Column(nullable = false)
    private boolean processed = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * 엔티티가 영속화되기 전에 생성 시각을 현재 시각으로 설정합니다.
     */
    @PrePersist
    void prePersist() {
        this.createdAt = Instant.now();
    }

}
