package com.sparta.myselectshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ✅ UserInfoDto 클래스는 사용자 정보를 담는 DTO입니다.
 *
 *    ➡️ 사용자 이름과 관리자 여부를 포함하며, 사용자 정보를 클라이언트에 반환할 때 사용됩니다.
 */
@Getter
@AllArgsConstructor
public class UserInfoDto {

    /**
     * ✅ 사용자의 사용자 이름입니다.
     *
     *    ➡️ 사용자 식별에 사용됩니다.
     */
    private String username;

    /**
     * ✅ 사용자가 관리자 권한을 가지는지 여부를 나타냅니다.
     *
     *    ➡️ true이면 관리자, false이면 일반 사용자입니다.
     */
    private boolean isAdmin;
}
