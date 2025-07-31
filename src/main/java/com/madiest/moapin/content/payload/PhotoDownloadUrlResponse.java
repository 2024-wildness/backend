package com.madiest.moapin.content.payload;

/**
 * Response DTO containing a presigned download URL for a photo.
 */
public class PhotoDownloadUrlResponse {

    private String url;

    public PhotoDownloadUrlResponse(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}