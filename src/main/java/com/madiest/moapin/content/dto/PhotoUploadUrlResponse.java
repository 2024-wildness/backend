package com.madiest.moapin.content.dto;

import lombok.Getter;

/** Response DTO containing the presigned URL and object key for photo upload. */
@Getter
public class PhotoUploadUrlResponse {

  private final String url;
  private final String key;

  /**
   * 사진 업로드를 위한 presigned URL과 객체 키를 포함하는 응답 객체를 생성합니다.
   *
   * @param url presigned URL 문자열
   * @param key 업로드 대상 객체의 키
   */
  public PhotoUploadUrlResponse(String url, String key) {
    this.url = url;
    this.key = key;
  }
}
