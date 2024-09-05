package com.sparta.myselectshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ✅ ApiUseTime은 API 사용 시간 데이터를 나타내는 엔티티 클래스입니다.
 *
 *    ➡️ 사용자의 API 사용 시간과 관련된 정보를 데이터베이스에 저장하고 관리하는 역할을 합니다.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "api_use_time") // 데이터베이스에서 이 엔티티에 대응되는 테이블 이름을 지정합니다.
public class ApiUseTime {

    /**
     * ✅ 엔티티의 고유 식별자입니다.
     *
     *    ➡️ 자동 생성되며 데이터베이스의 기본 키로 사용됩니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 생성 전략을 지정합니다.
    private Long id;

    /**
     * ✅ 사용자와의 일대일 관계를 설정합니다.
     *
     *    ➡️ 각 API 사용 시간 기록은 단일 사용자와 관련이 있습니다.
     *    ➡️ 'user_id'라는 외래 키 컬럼을 통해 사용자와 연관됩니다.
     */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false) // 외래 키를 'user_id'로 설정하고, null을 허용하지 않습니다.
    private User user;

    /**
     * ✅ API 사용 총 시간을 나타냅니다.
     *
     *    ➡️ API 사용 시간의 총합을 밀리초 단위로 저장합니다.
     */
    @Column(nullable = false) // null 값을 허용하지 않는 컬럼으로 설정합니다.
    private Long totalTime;

    /**
     * ✅ 생성자: 사용자와 총 사용 시간을 지정하여 ApiUseTime 객체를 생성합니다.
     *
     * @param user     이 API 사용 시간을 기록할 사용자 엔티티입니다.
     * @param totalTime API 사용 총 시간입니다.
     */
    public ApiUseTime(User user, Long totalTime) {
        this.user = user;
        this.totalTime = totalTime;
    }

    /**
     * ✅ API 사용 시간을 추가합니다.
     *
     *    ➡️ 현재 저장된 총 사용 시간에 주어진 사용 시간을 추가합니다.
     *
     * @param useTime 추가할 사용 시간입니다.
     */
    public void addUseTime(long useTime) {
        this.totalTime += useTime;
    }
}