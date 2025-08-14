package com.madiest.moapin.auth.dto;

import lombok.Getter;
import lombok.Setter;

/** Response payload containing the JWT access token. */
@Setter
@Getter
public class LoginResponse {

  private String accessToken;

  /**
   * 주어진 액세스 토큰으로 LoginResponse 객체를 생성합니다.
   *
   * @param accessToken JWT 액세스 토큰 문자열
   */
  public LoginResponse(String accessToken) {
    this.accessToken = accessToken;
  }
}
