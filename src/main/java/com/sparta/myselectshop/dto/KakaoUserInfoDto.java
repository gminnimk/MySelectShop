package com.sparta.myselectshop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ✅ KakaoUserInfoDto 클래스는 카카오 사용자 정보를 담는 DTO 입니다.
 *
 *    ➡️ 카카오 API로부터 받은 사용자 ID, 닉네임, 이메일 정보를 담고 있으며,
 *        이 정보를 다른 계층으로 전달하기 위해 사용됩니다.
 */
@Getter // 각 필드에 대한 Getter 메서드를 자동으로 생성합니다.
@NoArgsConstructor // 파라미터가 없는 기본 생성자를 자동으로 생성합니다.
public class KakaoUserInfoDto {
    private Long id; // 카카오 사용자 고유 ID
    private String nickname; // 카카오 사용자 닉네임
    private String email; // 카카오 사용자 이메일

    /**
     * ✅ 사용자 정보를 초기화하는 생성자입니다.
     *
     *    ➡️ 이 생성자는 모든 필드를 초기화하며, 주로 카카오 API로부터 받은 데이터를 기반으로 객체를 생성할 때 사용됩니다.
     *
     * @param id 카카오 사용자 고유 ID입니다.
     * @param nickname 카카오 사용자 닉네임입니다.
     * @param email 카카오 사용자 이메일입니다.
     */
    public KakaoUserInfoDto(Long id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }
}