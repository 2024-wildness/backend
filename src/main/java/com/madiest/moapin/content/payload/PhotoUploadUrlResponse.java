package com.madiest.moapin.content.payload;

/**
 * Response DTO containing the presigned URL and object key for photo upload.
 */
public class PhotoUploadUrlResponse {

    private String url;
    private String key;

    public PhotoUploadUrlResponse(String url, String key) {
        this.url = url;
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public String getKey() {
        return key;
    }
}