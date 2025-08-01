package com.madiest.moapin.content.payload;

import lombok.Getter;

/**
 * Response DTO containing a presigned download URL for a photo.
 */
@Getter
public class PhotoDownloadUrlResponse {

    private final String url;

    /**
     * PhotoDownloadUrlResponse 객체를 생성하며, 사진 다운로드를 위한 URL을 설정합니다.
     *
     * @param url 사진 다운로드에 사용할 프리사인드 URL
     */
    public PhotoDownloadUrlResponse(String url) {
        this.url = url;
    }

}