package com.sparta.myselectshop.repository;

import com.sparta.myselectshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;


// 이 인터페이스는 Spring Data JPA를 사용하여 Product 엔티티에 대한 데이터베이스 작업을 수행합니다.


/*
요약
ProductRepository 인터페이스는 Product 엔티티에 대한 데이터베이스 작업을 처리하기 위해 Spring Data JPA가 제공하는 JpaRepository 인터페이스를 확장합니다.
이 인터페이스를 통해 별도의 구현 없이도 기본적인 CRUD 작업을 손쉽게 수행할 수 있습니다.

CRUD 작업: JpaRepository는 기본적으로 save(), findById(), findAll(), deleteById() 등의 메서드를 제공합니다.
간편한 사용: Spring Data JPA가 이 인터페이스를 기반으로 런타임에 자동으로 구현체를 생성하므로, 복잡한 SQL 쿼리 없이도 데이터베이스 작업을 수행할 수 있습니다.
예를 들어, ProductRepository 인터페이스를 사용하면 Product 엔티티를 데이터베이스에 저장하거나, 조회하거나, 업데이트하거나, 삭제하는 작업을 쉽게 할 수 있습니다.
서비스 계층이나 컨트롤러에서 이 레포지토리를 주입받아 사용할 수 있습니다.
 */



// public interface ProductRepository: 이 인터페이스의 이름은 ProductRepository입니다.
// extends JpaRepository<Product, Long>: JpaRepository 인터페이스를 확장합니다.
// 여기서 Product는 이 레포지토리가 다룰 엔티티 클래스의 타입이고, Long은 엔티티의 기본 키 타입입니다.

public interface ProductRepository extends JpaRepository<Product, Long> {
}
