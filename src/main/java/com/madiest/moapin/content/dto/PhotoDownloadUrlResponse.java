package com.madiest.moapin.content.dto;

import lombok.Getter;

/** Response DTO containing a presigned download URL for a photo. */
@Getter
public class PhotoDownloadUrlResponse {

  private final String url;

  /**
   * 주어진 URL로 PhotoDownloadUrlResponse 객체를 생성합니다.
   *
   * @param url 사진 다운로드를 위한 프리사인드 URL
   */
  public PhotoDownloadUrlResponse(String url) {
    this.url = url;
  }
}
