package com.sparta.myselectshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// 이 클래스는 JPA 엔티티에 생성 및 수정 시간을 자동으로 기록하는 기능을 추가합니다.

/*
Timestamped 클래스는 JPA 엔티티에 생성 및 수정 시간을 자동으로 기록하는 기능을 추가합니다. 이 클래스는 추상 클래스로,
직접 인스턴스를 만들 수는 없지만, 이를 상속받는 다른 엔티티 클래스들은 createdAt과 modifiedAt 필드를 상속받아 자동으로 생성 및 수정 시간을 기록할 수 있습니다.
 */


@Getter // Lombok 애노테이션으로, 이 클래스의 모든 필드에 대한 getter 메서드를 자동으로 생성합니다.


// JPA 애노테이션으로, 이 클래스를 상속받는 엔티티 클래스들이 이 클래스의 필드를 상속받도록 합니다.
// 즉, 이 클래스 자체는 엔티티가 아니지만, 이 클래스를 상속받는 다른 클래스들이 엔티티가 됩니다.
@MappedSuperclass


// 이 클래스에 auditing 기능을 추가합니다. 이 기능을 통해 엔티티가 생성되거나 수정될 때 자동으로 시간을 기록할 수 있습니다.
@EntityListeners(AuditingEntityListener.class)


public abstract class Timestamped {

    @CreatedDate // 엔티티가 생성될 때 자동으로 시간을 기록합니다.
    @Column(updatable = false) // 이 필드는 엔티티가 생성된 후에는 수정할 수 없습니다.
    @Temporal(TemporalType.TIMESTAMP) // 이 필드가 타임스탬프 형식으로 저장되도록 설정합니다.
    private LocalDateTime createdAt; // 엔티티가 생성된 시간을 저장합니다.

    @LastModifiedDate // 엔티티가 수정될 때 자동으로 시간을 기록합니다.
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modifiedAt; // 엔티티가 마지막으로 수정된 시간을 저장합니다.
}