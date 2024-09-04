package com.sparta.myselectshop.repository;

import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ✅ ProductRepository 인터페이스는 Product 엔티티에 대한 CRUD (생성, 조회, 업데이트, 삭제) 작업을 처리하는 리포지토리입니다.
 *    ➡️ Spring Data JPA의 JpaRepository를 상속받아 기본적인 데이터베이스 작업을 자동으로 제공합니다.
 *    ➡️ 이 인터페이스는 Product 엔티티를 데이터베이스에 저장하거나 조회하는 데 사용됩니다.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * ✅ 특정 사용자에 의해 등록된 모든 상품을 조회합니다.
     *    ➡️ 주어진 사용자와 연관된 상품 목록을 반환합니다.
     *
     * @param user 상품을 등록한 사용자 객체입니다.
     * @return List<Product> 주어진 사용자에 의해 등록된 모든 상품 목록입니다.
     */
    List<Product> findAllByUser(User user);
}