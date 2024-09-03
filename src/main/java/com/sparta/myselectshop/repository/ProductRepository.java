package com.sparta.myselectshop.repository;

import com.sparta.myselectshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ✅ ProductRepository 인터페이스는 Product 엔티티에 대한 CRUD (생성, 조회, 업데이트, 삭제) 작업을 처리하는 리포지토리입니다.
 *
 *    ➡️ Spring Data JPA의 JpaRepository를 상속받아 기본적인 데이터베이스 작업을 자동으로 제공합니다.
 *    ➡️ 이 인터페이스는 Product 엔티티를 데이터베이스에 저장하거나 조회하는 데 사용됩니다.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
    // JpaRepository에서 제공하는 기본적인 CRUD 메서드가 자동으로 포함됩니다.
    // 예: save(), findById(), findAll(), deleteById() 등
}