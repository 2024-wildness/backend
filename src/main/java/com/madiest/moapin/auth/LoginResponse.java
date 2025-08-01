package com.madiest.moapin.auth;

import lombok.Getter;
import lombok.Setter;

/**
 * Response payload containing the JWT access token.
 */
@Setter
@Getter
public class LoginResponse {

    private String accessToken;

    /**
     * LoginResponse 객체를 주어진 액세스 토큰으로 초기화합니다.
     *
     * @param accessToken JWT 액세스 토큰 문자열
     */
    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }

}