package com.sparta.myselectshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * ✅ Timestamped 클래스는 엔티티의 생성 및 수정 일시를 자동으로 기록하는 추상 클래스입니다.
 *
 *    ➡️ JPA 엔티티에서 생성 및 수정 일시를 추적하기 위해 사용됩니다. 이 클래스는 직접 인스턴스화되지 않고,
 *      다른 엔티티 클래스에서 상속받아 사용됩니다.
 */
@Getter
@MappedSuperclass // 이 클래스는 매핑된 슈퍼클래스를 정의합니다. 실제 테이블과 매핑되지 않으며, 상속받는 클래스에 필드를 제공합니다.
@EntityListeners(AuditingEntityListener.class) // JPA의 Auditing 기능을 사용하여 자동으로 생성 및 수정 일시를 기록합니다.
public abstract class Timestamped {

    @CreatedDate // 엔티티가 생성될 때 자동으로 생성 일시가 기록됩니다.
    @Column(updatable = false) // 생성 일시는 업데이트할 수 없도록 설정합니다.
    @Temporal(TemporalType.TIMESTAMP) // 날짜와 시간 정보를 TIMESTAMP 형식으로 저장합니다.
    private LocalDateTime createdAt; // 엔티티의 생성 일시

    @LastModifiedDate // 엔티티가 수정될 때 자동으로 수정 일시가 기록됩니다.
    @Column // 수정 일시는 업데이트가 가능하도록 설정됩니다.
    @Temporal(TemporalType.TIMESTAMP) // 날짜와 시간 정보를 TIMESTAMP 형식으로 저장합니다.
    private LocalDateTime modifiedAt; // 엔티티의 수정 일시
}
