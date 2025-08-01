package com.madiest.moapin.content.payload;

import lombok.Getter;

/**
 * Response DTO containing the presigned URL and object key for photo upload.
 */
@Getter
public class PhotoUploadUrlResponse {

    private final String url;
    private final String key;

    public PhotoUploadUrlResponse(String url, String key) {
        this.url = url;
        this.key = key;
    }

}