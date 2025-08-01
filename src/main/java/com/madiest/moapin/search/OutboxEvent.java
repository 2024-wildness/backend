package com.madiest.moapin.search;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Entity representing an event to be processed by the search indexer.
 */
@Getter
@Entity
@Table(name = "outbox_event")
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Type of event: INDEX or DELETE */
    @Setter
    @Column(nullable = false)
    private String type;

    /** ID of the affected content item */
    @Setter
    @Column(name = "content_id", nullable = false)
    private Long contentId;

    /** JSON payload representing the document to index */
    @Setter
    @Lob
    private String payload;

    @Setter
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
