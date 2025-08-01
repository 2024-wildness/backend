package com.madiest.moapin.share;

import com.madiest.moapin.content.Content;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Entity representing a shareable link for a content item.
 */
@Getter
@Entity
@Table(name = "share_link")
public class ShareLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, unique = true)
    private String token;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "content_id")
    private Content content;

    @Setter
    @Column(name = "expires_at")
    private Instant expiresAt;

    @Setter
    @Column(name = "max_downloads")
    private Integer maxDownloads;

    @Setter
    @Column(name = "download_count", nullable = false)
    private long downloadCount = 0L;

    @Setter
    @Column(nullable = false)
    private boolean revoked = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        this.createdAt = Instant.now();
    }

}
