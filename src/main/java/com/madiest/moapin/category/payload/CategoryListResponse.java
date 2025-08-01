package com.madiest.moapin.category.payload;

import lombok.Getter;

import java.time.Instant;

/**
 * DTO for returning category list entries with metadata.
 */
@Getter
public class CategoryListResponse {

    private final Long id;
    private final String name;
    private final Integer orderIndex;
    private final Instant createdAt;
    private final long contentCount;

    public CategoryListResponse(Long id,
                                String name,
                                Integer orderIndex,
                                Instant createdAt,
                                long contentCount) {
        this.id = id;
        this.name = name;
        this.orderIndex = orderIndex;
        this.createdAt = createdAt;
        this.contentCount = contentCount;
    }

}