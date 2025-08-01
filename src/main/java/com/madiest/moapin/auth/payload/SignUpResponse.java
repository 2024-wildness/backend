package com.madiest.moapin.auth.payload;

import lombok.Getter;

/**
 * Response payload for user registration.
 */
@Getter
public class SignUpResponse {

    private String message;

    /**
     * 회원가입 요청에 대한 응답 메시지를 포함하는 SignUpResponse 객체를 생성합니다.
     *
     * @param message 응답 메시지
     */
    public SignUpResponse(String message) {
        this.message = message;
    }

    /**
     * 회원가입 응답 메시지를 설정합니다.
     *
     * @param message 응답 메시지로 설정할 문자열
     */
    public void setMessage(String message) {
        this.message = message;
    }
}