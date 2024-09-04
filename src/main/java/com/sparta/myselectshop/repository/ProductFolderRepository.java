package com.sparta.myselectshop.repository;

import com.sparta.myselectshop.entity.ProductFolder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ✅ ProductFolderRepository는 `ProductFolder` 엔티티에 대한 데이터베이스 작업을 처리하는 JPA 리포지토리입니다.
 *
 *    ➡️ Spring Data JPA의 `JpaRepository`를 확장하여 기본적인 CRUD (Create, Read, Update, Delete) 작업을 지원합니다.
 *    ➡️ `ProductFolder` 엔티티와 `Long` 타입의 기본 키를 사용하는 리포지토리입니다.
 */
public interface ProductFolderRepository extends JpaRepository<ProductFolder, Long> {
    // 기본적인 CRUD 작업은 JpaRepository에 의해 자동으로 제공됩니다.
    // 추가적인 쿼리 메서드를 정의할 수 있습니다.
}