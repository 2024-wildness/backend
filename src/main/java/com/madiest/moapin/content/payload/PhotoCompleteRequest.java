package com.madiest.moapin.content.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Request DTO for completing a photo upload and creating the Photo entity.
 */
public class PhotoCompleteRequest {

    @NotBlank
    private String key;

    private Long categoryId;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}