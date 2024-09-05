package com.sparta.myselectshop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * ✅ JpaConfig 클래스는 Spring Data JPA의 감사 기능을 활성화하기 위한 설정 클래스입니다.
 *
 *    ➡️ 이 설정을 통해 엔티티의 생성 및 수정 시간 등을 자동으로 관리할 수 있습니다.
 */
@Configuration // Spring의 설정 클래스를 정의하는 어노테이션입니다.
// 이 어노테이션을 통해 Spring 컨텍스트에서 이 클래스를 빈으로 등록합니다.
@EnableJpaAuditing // JPA 엔티티의 생성 및 수정 시간 자동 기록을 활성화하는 어노테이션입니다.
// 이 어노테이션을 사용하면 엔티티의 `@CreatedDate` 및 `@LastModifiedDate` 애너테이션을 지원합니다.
public class JpaConfig {
    // 이 클래스는 추가적인 설정 없이 JpaAuditing을 활성화합니다.
}
