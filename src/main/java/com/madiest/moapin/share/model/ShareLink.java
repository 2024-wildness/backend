package com.madiest.moapin.share.model;

import com.madiest.moapin.content.model.Content;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShareLink {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    private String token;

    private Instant expiresAt;

    private Integer maxDownloads;

    private int downloadCount;

    public ShareLink(Content content, Instant expiresAt, Integer maxDownloads) {
        this.id = UUID.randomUUID().toString();
        this.token = UUID.randomUUID().toString().replace("-", "");
        this.content = content;
        this.expiresAt = expiresAt;
        this.maxDownloads = maxDownloads;
    }

    public void incrementDownloadCount() {
        this.downloadCount++;
    }
}
