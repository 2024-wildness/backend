package com.madiest.moapin.content.payload;

import com.madiest.moapin.content.*;
import lombok.Getter;

import java.time.Instant;

/**
 * Response DTO for content items.
 */
@Getter
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
        switch (content) {
            case Photo photo -> {
                resp.type = ContentType.PHOTO;
                resp.fileKey = photo.getFileKey();
            }
            case Link link -> {
                resp.type = ContentType.LINK;
                resp.url = link.getUrl();
            }
            case Note note -> {
                resp.type = ContentType.NOTE;
                resp.textContent = note.getTextContent();
            }
            default -> {
            }
        }
        return resp;
    }

}