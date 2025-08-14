package com.madiest.moapin.category.dto;

import com.madiest.moapin.category.model.Category;
import java.time.Instant;
import lombok.Getter;

/** DTO for returning category list entries with metadata. */
@Getter
public class CategoryListResponse {

  private final Long id;
  private final String name;
  private final Integer orderIndex;
  private final Instant createdAt;
  private final long contentCount;

  /**
   * 카테고리 목록 항목의 정보를 포함하는 CategoryListResponse 객체를 생성합니다.
   *
   * @param id 카테고리의 고유 식별자
   * @param name 카테고리 이름
   * @param orderIndex 카테고리 정렬 순서
   * @param createdAt 카테고리 생성 시각
   * @param contentCount 해당 카테고리에 포함된 콘텐츠 개수
   */
  public CategoryListResponse(
      Long id, String name, Integer orderIndex, Instant createdAt, long contentCount) {
    this.id = id;
    this.name = name;
    this.orderIndex = orderIndex;
    this.createdAt = createdAt;
    this.contentCount = contentCount;
  }

  public static CategoryListResponse from(Category category) {
    return new CategoryListResponse(
        category.getId(),
        category.getName(),
        category.getOrderIndex(),
        category.getCreatedAt(),
        (long) category.getContents().size());
  }
}
