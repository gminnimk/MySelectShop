package com.sparta.myselectshop.entity;

/**
 * ✅ UserRoleEnum 열거형 클래스는 사용자의 권한을 정의합니다.
 *
 *    ➡️ 사용자와 관리자의 두 가지 권한을 제공하며,
 *      각 권한에 대한 역할을 문자열 상수로 정의합니다.
 */
public enum UserRoleEnum {

    /**
     * ✅ 일반 사용자 권한을 나타냅니다.
     *
     *    ➡️ 사용자 권한은 "ROLE_USER" 문자열 상수로 정의됩니다.
     */
    USER(Authority.USER),

    /**
     * ✅ 관리자 권한을 나타냅니다.
     *
     *    ➡️ 관리자 권한은 "ROLE_ADMIN" 문자열 상수로 정의됩니다.
     */
    ADMIN(Authority.ADMIN);

    private final String authority;

    /**
     * ✅ UserRoleEnum의 생성자입니다.
     *
     *    ➡️ 권한을 설정합니다.
     *
     * @param authority 권한을 나타내는 문자열
     */
    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    /**
     * ✅ 권한 문자열을 반환합니다.
     *
     *    ➡️ 이 메서드는 해당 열거형 상수의 권한 문자열을 반환합니다.
     *
     * @return 권한을 나타내는 문자열
     */
    public String getAuthority() {
        return this.authority;
    }

    /**
     * ✅ 권한 문자열 상수를 정의하는 내부 클래스입니다.
     *
     *    ➡️ 사용자와 관리자 권한에 대한 문자열 상수를 포함합니다.
     */
    public static class Authority {
        public static final String USER = "ROLE_USER";  // 일반 사용자 권한 문자열
        public static final String ADMIN = "ROLE_ADMIN"; // 관리자 권한 문자열
    }
}