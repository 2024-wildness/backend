package com.madiest.moapin.content.dto;

import com.madiest.moapin.content.model.Content;
import com.madiest.moapin.content.model.ContentType;
import com.madiest.moapin.content.model.Link;
import com.madiest.moapin.content.model.Note;
import com.madiest.moapin.content.model.Photo;
import java.time.Instant;
import lombok.Getter;

/** Response DTO for content items. */
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

  /**
   * 주어진 Content 엔티티를 기반으로 ContentResponse 객체를 생성합니다.
   *
   * <p>Content의 실제 타입에 따라 type 및 관련 필드를 설정합니다.
   *
   * @param content Content 엔티티 인스턴스
   * @return ContentResponse로 변환된 객체
   */
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
        resp.textContent = note.getBody();
      }
      default -> {}
    }
    return resp;
  }
}
