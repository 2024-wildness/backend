package com.madiest.moapin.auth.dto;

import lombok.Getter;

/**
 * Response payload for user registration.
 */
@Getter
public class SignUpResponse {

    private String message;

    /**
     * 새로운 회원가입 응답 객체를 생성합니다.
     *
     * @param message 회원가입 결과 메시지
     */
    public SignUpResponse(String message) {
        this.message = message;
    }

    /**
     * 회원가입 응답 메시지를 설정합니다.
     *
     * @param message 설정할 메시지 내용
     */
    public void setMessage(String message) {
        this.message = message;
    }
}