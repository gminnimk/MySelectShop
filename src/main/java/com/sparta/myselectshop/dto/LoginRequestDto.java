package com.sparta.myselectshop.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * ✅ LoginRequestDto 클래스는 사용자 로그인 요청에 필요한 데이터를 담는 DTO입니다.
 *
 *    ➡️ 클라이언트가 서버에 로그인 요청을 보낼 때 필요한 정보를 포함합니다.
 */
@Setter
@Getter
public class LoginRequestDto {

    /**
     * ✅ 사용자의 사용자 이름입니다.
     *
     *    ➡️ 로그인 시 사용자 식별을 위해 필요합니다.
     */
    private String username;

    /**
     * ✅ 사용자의 비밀번호입니다.
     *
     *    ➡️ 로그인 시 사용자 인증을 위해 필요합니다.
     */
    private String password;
}