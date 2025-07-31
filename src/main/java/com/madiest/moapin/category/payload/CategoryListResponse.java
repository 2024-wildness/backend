package com.madiest.moapin.category.payload;

import java.time.Instant;

/**
 * DTO for returning category list entries with metadata.
 */
public class CategoryListResponse {

    private Long id;
    private String name;
    private Integer orderIndex;
    private Instant createdAt;
    private long contentCount;

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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public long getContentCount() {
        return contentCount;
    }
}