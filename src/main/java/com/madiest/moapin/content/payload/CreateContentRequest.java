package com.madiest.moapin.content.payload;

import com.madiest.moapin.content.ContentType;
import javax.validation.constraints.NotNull;

/**
 * Request DTO for creating a content item.
 */
public class CreateContentRequest {

    @NotNull
    private ContentType type;

    private Long categoryId;

    private String fileKey;

    private String url;

    private String textContent;

    public ContentType getType() {
        return type;
    }

    public void setType(ContentType type) {
        this.type = type;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
}