package com.madiest.moapin.share;

import com.madiest.moapin.content.Content;

import javax.persistence.*;
import java.time.Instant;

/**
 * Entity representing a shareable link for a content item.
 */
@Entity
@Table(name = "share_link")
public class ShareLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(optional = false)
    @JoinColumn(name = "content_id")
    private Content content;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "max_downloads")
    private Integer maxDownloads;

    @Column(name = "download_count", nullable = false)
    private long downloadCount = 0L;

    @Column(nullable = false)
    private boolean revoked = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * 엔티티가 처음 저장되기 전에 생성 시각을 현재 시각으로 설정합니다.
     */
    @PrePersist
    void prePersist() {
        this.createdAt = Instant.now();
    }

    /**
 * 이 공유 링크의 고유 식별자를 반환합니다.
 *
 * @return 공유 링크의 ID
 */
public Long getId() { return id; }
    /**
 * 이 공유 링크의 고유 토큰 값을 반환합니다.
 *
 * @return 공유 링크를 식별하는 고유 토큰 문자열
 */
public String getToken() { return token; }
    /**
 * 공유 링크의 토큰 값을 설정합니다.
 *
 * @param token 새로 설정할 토큰 문자열
 */
public void setToken(String token) { this.token = token; }
    /**
 * 이 공유 링크와 연결된 콘텐츠 엔티티를 반환합니다.
 *
 * @return 연결된 Content 엔티티
 */
public Content getContent() { return content; }
    /**
 * 이 공유 링크와 연결된 콘텐츠 엔터티를 설정합니다.
 *
 * @param content 연결할 콘텐츠 엔터티
 */
public void setContent(Content content) { this.content = content; }
    /**
 * 이 공유 링크의 만료 시간을 반환합니다.
 *
 * @return 만료 시간(`Instant`) 또는 만료 시간이 설정되지 않은 경우 `null`
 */
public Instant getExpiresAt() { return expiresAt; }
    /**
 * 공유 링크의 만료 시간을 설정합니다.
 *
 * @param expiresAt 만료 시각(UTC, null이면 만료 없음)
 */
public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
    /**
 * 이 공유 링크의 최대 다운로드 횟수를 반환합니다.
 *
 * @return 최대 다운로드 횟수 또는 제한이 없으면 null
 */
public Integer getMaxDownloads() { return maxDownloads; }
    /**
 * 최대 다운로드 횟수 제한을 설정합니다.
 *
 * @param maxDownloads 허용할 최대 다운로드 횟수 (null이면 제한 없음)
 */
public void setMaxDownloads(Integer maxDownloads) { this.maxDownloads = maxDownloads; }
    /**
 * 이 공유 링크의 현재 다운로드 횟수를 반환합니다.
 *
 * @return 다운로드된 횟수
 */
public long getDownloadCount() { return downloadCount; }
    /**
 * 다운로드 횟수를 설정합니다.
 *
 * @param downloadCount 새로운 다운로드 횟수 값
 */
public void setDownloadCount(long downloadCount) { this.downloadCount = downloadCount; }
    /**
 * 이 공유 링크가 취소(무효화)되었는지 여부를 반환합니다.
 *
 * @return 공유 링크가 취소된 경우 true, 그렇지 않으면 false
 */
public boolean isRevoked() { return revoked; }
    /**
 * 공유 링크의 폐기(revoked) 상태를 설정합니다.
 *
 * @param revoked 공유 링크가 폐기되었는지 여부
 */
public void setRevoked(boolean revoked) { this.revoked = revoked; }
    /**
 * 이 공유 링크가 생성된 시각을 반환합니다.
 *
 * @return 공유 링크의 생성 시각
 */
public Instant getCreatedAt() { return createdAt; }
}
