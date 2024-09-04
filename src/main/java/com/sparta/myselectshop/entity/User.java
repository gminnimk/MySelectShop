package com.sparta.myselectshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ✅ User 엔티티 클래스는 애플리케이션의 사용자 정보를 나타냅니다.
 *
 *    ➡️ 이 클래스는 사용자와 관련된 기본 정보를 데이터베이스에 저장하는 데 사용됩니다.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

    /**
     * ✅ 사용자 고유 ID입니다.
     *
     *    ➡️ 데이터베이스에서 자동으로 생성되는 기본 키입니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ✅ 사용자의 사용자 이름입니다.
     *
     *    ➡️ 유일하며, 빈 값을 허용하지 않습니다.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * ✅ 사용자의 비밀번호입니다.
     *
     *    ➡️ 빈 값을 허용하지 않습니다.
     */
    @Column(nullable = false)
    private String password;

    /**
     * ✅ 사용자의 이메일 주소입니다.
     *
     *    ➡️ 유일하며, 빈 값을 허용하지 않습니다.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * ✅ 사용자의 권한을 나타내는 열거형입니다.
     *
     *    ➡️ 권한을 문자열로 저장하며, 빈 값을 허용하지 않습니다.
     */
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    /**
     * ✅ 모든 필드를 초기화하는 생성자입니다.
     *
     *    ➡️ 사용자 객체를 생성할 때 필요한 정보를 설정합니다.
     *
     * @param username 사용자 이름
     * @param password 비밀번호
     * @param email 이메일 주소
     * @param role 사용자 권한
     */
    public User(String username, String password, String email, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}