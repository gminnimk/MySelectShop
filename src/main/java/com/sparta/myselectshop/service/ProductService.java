package com.sparta.myselectshop.service;


import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


// 이 서비스는 주로 Product 엔티티와 관련된 비즈니스 로직을 처리합니다.


/*
요약
ProductService 클래스는 제품 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
주로 Product 엔티티와 관련된 작업을 수행하며, 데이터베이스와의 상호작용을 위해 ProductRepository를 사용합니다.


주요 기능: createProduct 메서드는 ProductRequestDto 객체를 받아 새로운 Product 객체를 생성하고 데이터베이스에 저장한 후, ProductResponseDto 객체로 변환하여 반환합니다.
Spring Bean 등록: @Service 애노테이션을 통해 Spring Bean으로 등록되며, @RequiredArgsConstructor
애노테이션을 통해 final 필드에 대한 생성자를 자동으로 생성하여 의존성을 주입합니다.
이 클래스는 주로 컨트롤러에서 호출되어 클라이언트의 요청을 처리하고, 그 결과를 클라이언트에게 반환하는 역할을 합니다.
 */

@Service // 이 클래스가 서비스 레이어 클래스임을 나타냅니다. Spring은 이 애노테이션을 보고 이 클래스를 서비스 빈으로 등록합니다.
@RequiredArgsConstructor // Lombok 애노테이션으로, final로 선언된 필드에 대한 생성자를 자동으로 생성합니다.
public class ProductService {


    // roductRepository 인스턴스를 주입받아 사용합니다. 이 필드는 @RequiredArgsConstructor에 의해 생성자 주입 방식으로 초기화됩니다.
    private final ProductRepository productRepository;


    public ProductResponseDto createProduct(ProductRequestDto requestDto) { // 클라이언트로부터 전달받은 ProductRequestDto 객체를 바탕으로 새로운 제품을 생성하는 메서드입니다.
        Product product = productRepository.save(new Product(requestDto)); // ProductRequestDto 객체를 사용해 새로운 Product 객체를 생성하고, 이를 productRepository를 통해 데이터베이스에 저장합니다.
        return new ProductResponseDto(product); // 저장된 Product 객체를 ProductResponseDto로 변환하여 반환합니다. 이는 클라이언트에게 저장된 제품 정보를 응답하기 위함입니다.
    }
}
