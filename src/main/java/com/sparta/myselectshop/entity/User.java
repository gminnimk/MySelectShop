package com.sparta.myselectshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ✅ User 클래스는 사용자 정보를 저장하는 엔티티(Entity)입니다.
 *
 *    ➡️ 이 클래스는 데이터베이스의 'users' 테이블과 매핑되며, 사용자 ID, 사용자명, 비밀번호, 이메일,
 *       역할(UserRoleEnum) 및 카카오 ID와 같은 필드를 포함하고 있습니다.
 */
@Entity // 이 클래스가 JPA 엔티티임을 나타냄
@Getter
@Setter
@NoArgsConstructor // Lombok 어노테이션: 파라미터가 없는 기본 생성자 자동 생성
@Table(name = "users") // 데이터베이스에서 'users' 테이블과 매핑
public class User {
    @Id // 기본 키(primary key)로 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 값을 자동 생성, IDENTITY 전략 사용
    private Long id; // 사용자 ID (자동 증가)

    @Column(nullable = false, unique = true) // 'username' 컬럼: null 값 불가, 유니크 제약 조건
    private String username; // 사용자명 (로그인 ID로 사용)

    @Column(nullable = false) // 'password' 컬럼: null 값 불가
    private String password; // 비밀번호 (암호화된 상태로 저장)

    @Column(nullable = false, unique = true) // 'email' 컬럼: null 값 불가, 유니크 제약 조건
    private String email; // 사용자 이메일

    @Column(nullable = false) // 'role' 컬럼: null 값 불가
    @Enumerated(value = EnumType.STRING) // EnumType.STRING으로 열거형 값을 문자열로 저장
    private UserRoleEnum role; // 사용자 권한 (ADMIN 또는 USER)

    // 카카오 로그인 연동을 위한 필드, null일 수 있음
    private Long kakaoId; // 카카오 ID (카카오 로그인 사용자의 경우 저장)

    /**
     * ✅ 사용자 정보를 받아서 User 객체를 생성하는 생성자 (카카오 ID 제외)
     *
     * @param username 사용자명
     * @param password 비밀번호
     * @param email 이메일
     * @param role 사용자 권한
     */
    public User(String username, String password, String email, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    /**
     * ✅ 사용자 정보를 받아서 User 객체를 생성하는 생성자 (카카오 ID 포함)
     *
     * @param username 사용자명
     * @param password 비밀번호
     * @param email 이메일
     * @param role 사용자 권한
     * @param kakaoId 카카오 ID
     */
    public User(String username, String password, String email, UserRoleEnum role, Long kakaoId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.kakaoId = kakaoId;
    }

    /**
     * ✅ 카카오 ID를 업데이트하는 메서드
     *
     *    ➡️ 기존 사용자의 카카오 ID를 새로 업데이트합니다.
     *
     * @param kakaoId 업데이트할 카카오 ID
     * @return 업데이트된 User 객체를 반환
     */
    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this; // 메서드 체이닝을 위해 this 반환
    }
}