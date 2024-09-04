package com.sparta.myselectshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ✅ Folder 엔티티 클래스는 사용자의 폴더 정보를 나타냅니다.
 *
 *    ➡️ 각 폴더는 사용자의 소속 폴더를 표현하며, 데이터베이스에 저장됩니다.
 */
@Entity // 이 클래스가 JPA 엔티티임을 나타냅니다.
@Getter
@Setter
@NoArgsConstructor // Lombok 어노테이션으로 기본 생성자를 자동으로 생성합니다.
@Table(name = "folder") // 데이터베이스 테이블 이름을 "folder"로 설정합니다.
public class Folder {

    /**
     * ✅ 폴더의 고유 ID입니다.
     *    ➡️ 데이터베이스에서 자동으로 생성되는 기본 키입니다.
     */
    @Id // 이 필드가 엔티티의 기본 키임을 나타냅니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성 전략을 데이터베이스의 자동 증가로 설정합니다.
    private Long id;

    /**
     * ✅ 폴더의 이름입니다.
     *
     *    ➡️ 빈 값을 허용하지 않으며, 폴더의 식별에 사용됩니다.
     */
    @Column(nullable = false) // 데이터베이스 컬럼으로 매핑되며, 빈 값을 허용하지 않습니다.
    private String name;

    /**
     * ✅ 폴더에 소속된 사용자입니다.
     *
     *    ➡️ 이 폴더를 소유하는 사용자와의 관계를 나타냅니다.
     */
    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계를 나타내며, 지연 로딩을 설정합니다.
    @JoinColumn(name = "user_id", nullable = false) // 외래 키 컬럼으로 매핑되며, 빈 값을 허용하지 않습니다.
    private User user;

    /**
     * ✅ 모든 필드를 초기화하는 생성자입니다.
     *
     *    ➡️ 폴더 객체를 생성할 때 폴더의 이름과 소속 사용자를 설정합니다.
     *
     * @param name 폴더의 이름
     * @param user 폴더를 소유하는 사용자 객체
     */
    public Folder(String name, User user) {
        this.name = name;
        this.user = user;
    }
}