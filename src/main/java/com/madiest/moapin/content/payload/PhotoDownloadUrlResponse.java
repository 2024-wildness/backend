package com.madiest.moapin.content.payload;

import lombok.Getter;

/**
 * Response DTO containing a presigned download URL for a photo.
 */
@Getter
public class PhotoDownloadUrlResponse {

    private final String url;

    public PhotoDownloadUrlResponse(String url) {
        this.url = url;
    }

}