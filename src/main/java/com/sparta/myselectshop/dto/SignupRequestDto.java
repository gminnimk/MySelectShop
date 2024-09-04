package com.sparta.myselectshop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * ✅ SignupRequestDto 클래스는 사용자 가입 요청에 필요한 데이터를 담는 DTO입니다.
 *
 *    ➡️ 클라이언트가 사용자 가입을 위해 서버에 전달하는 정보를 포함합니다.
 */
@Getter
@Setter
public class SignupRequestDto {

    /**
     * ✅ 사용자의 사용자 이름입니다.
     *
     *    ➡️ 빈 값을 허용하지 않으며, 사용자 가입 시 필수 입력 사항입니다.
     */
    @NotBlank
    private String username;

    /**
     * ✅ 사용자의 비밀번호입니다.
     *
     *    ➡️ 빈 값을 허용하지 않으며, 사용자 가입 시 필수 입력 사항입니다.
     */
    @NotBlank
    private String password;

    /**
     * ✅ 사용자의 이메일 주소입니다.
     *
     *    ➡️ 빈 값을 허용하지 않으며, 유효한 이메일 형식을 요구합니다.
     */
    @Email
    @NotBlank
    private String email;

    /**
     * ✅ 관리자인지 여부를 나타냅니다.
     *
     *    ➡️ 기본값은 false이며, 관리자로 가입하려는 경우 true로 설정합니다.
     */
    private boolean admin = false;

    /**
     * ✅ 관리자 권한을 부여하기 위한 토큰입니다.
     *
     *    ➡️ 기본값은 빈 문자열이며, 관리자로 가입 시 필요한 경우 값을 설정합니다.
     */
    private String adminToken = "";
}