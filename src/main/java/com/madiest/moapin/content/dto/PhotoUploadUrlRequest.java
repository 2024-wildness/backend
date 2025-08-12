package com.madiest.moapin.content.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO to generate a presigned upload URL for a photo.
 */
public class PhotoUploadUrlRequest {

    @NotBlank
    private String fileName;

    private String contentType;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}