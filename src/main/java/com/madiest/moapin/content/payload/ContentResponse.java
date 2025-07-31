package com.madiest.moapin.content.payload;

import com.madiest.moapin.content.Content;
import com.madiest.moapin.content.Photo;
import com.madiest.moapin.content.Link;
import com.madiest.moapin.content.Note;
import com.madiest.moapin.content.ContentType;

import java.time.Instant;

/**
 * Response DTO for content items.
 */
public class ContentResponse {

    private Long id;
    private ContentType type;
    private Instant createdAt;
    private long viewCount;
    private boolean pinned;
    private Long categoryId;
    private String fileKey;
    private String url;
    private String textContent;

    public static ContentResponse fromEntity(Content content) {
        ContentResponse resp = new ContentResponse();
        resp.id = content.getId();
        resp.createdAt = content.getCreatedAt();
        resp.viewCount = content.getViewCount();
        resp.pinned = content.isPinned();
        resp.categoryId = content.getCategory() != null ? content.getCategory().getId() : null;
        if (content instanceof Photo) {
            resp.type = ContentType.PHOTO;
            resp.fileKey = ((Photo) content).getFileKey();
        } else if (content instanceof Link) {
            resp.type = ContentType.LINK;
            resp.url = ((Link) content).getUrl();
        } else if (content instanceof Note) {
            resp.type = ContentType.NOTE;
            resp.textContent = ((Note) content).getTextContent();
        }
        return resp;
    }

    public Long getId() {
        return id;
    }

    public ContentType getType() {
        return type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public long getViewCount() {
        return viewCount;
    }

    public boolean isPinned() {
        return pinned;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getFileKey() {
        return fileKey;
    }

    public String getUrl() {
        return url;
    }

    public String getTextContent() {
        return textContent;
    }
}