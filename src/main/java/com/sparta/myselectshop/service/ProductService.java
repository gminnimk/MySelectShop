package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * ✅ ProductService 클래스는 상품과 관련된 비즈니스 로직을 처리하는 서비스 클래스입니다.
 *
 *    ➡️ 상품 생성, 조회, 업데이트 등의 작업을 수행합니다.
 *    ➡️ ProductRepository를 통해 데이터베이스와 상호작용합니다.
 */
@Service // Spring의 서비스 컴포넌트로 등록됩니다.
@RequiredArgsConstructor // 생성자 주입을 위해 Lombok의 @RequiredArgsConstructor를 사용합니다.
public class ProductService {

    // ProductRepository를 주입받습니다.
    private final ProductRepository productRepository;

    /**
     * ✅ 새로운 상품을 생성합니다.
     *
     *    ➡️ 클라이언트로부터 받은 요청 데이터(ProductRequestDto)를 바탕으로 상품을 생성하고, 데이터베이스에 저장합니다.
     *    ➡️ 저장된 상품을 ProductResponseDto 형식으로 반환합니다.
     *
     * @param requestDto 상품 생성에 필요한 요청 데이터를 담고 있는 DTO입니다.
     * @return ProductResponseDto 생성된 상품의 정보를 포함하는 DTO입니다.
     */
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        // ProductRequestDto를 기반으로 Product 엔티티를 생성합니다.
        Product product = new Product(requestDto);

        // 생성된 Product 엔티티를 데이터베이스에 저장합니다.
        product = productRepository.save(product);

        // 저장된 상품을 기반으로 ProductResponseDto를 생성하여 반환합니다.
        return new ProductResponseDto(product);
    }
}