package com.madiest.moapin.content.payload;

import lombok.Getter;

/**
 * Response DTO containing the presigned URL and object key for photo upload.
 */
@Getter
public class PhotoUploadUrlResponse {

    private final String url;
    private final String key;

    /**
     * PhotoUploadUrlResponse 객체를 presigned URL과 객체 키로 초기화합니다.
     *
     * @param url 업로드에 사용할 presigned URL
     * @param key 업로드 대상 객체의 키
     */
    public PhotoUploadUrlResponse(String url, String key) {
        this.url = url;
        this.key = key;
    }

}