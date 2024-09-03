package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ✅ ProductController 클래스는 상품 관련 요청을 처리하는 컨트롤러입니다.
 *
 *    ➡️ 상품 생성과 같은 작업을 처리하며, 클라이언트의 요청을 서비스 계층으로 전달합니다.
 */
@RestController // 이 클래스가 RESTful 웹 서비스의 컨트롤러임을 나타냅니다.
@RequiredArgsConstructor // Lombok 어노테이션으로, 모든 final 필드를 사용하는 생성자를 자동으로 생성합니다.
@RequestMapping("/api") // 모든 메서드는 "/api" 경로를 기본으로 합니다.
public class ProductController {

    private final ProductService productService; // 상품 관련 비즈니스 로직을 처리하는 서비스 클래스

    /**
     * ✅ 새로운 상품을 생성하는 API 엔드포인트입니다.
     *
     *    ➡️ 클라이언트로부터 상품 정보를 포함하는 요청 본문을 받아서 상품을 생성하고, 생성된 상품의 정보를 반환합니다.
     *
     * @param requestDto 클라이언트가 제공한 상품 생성 요청 데이터입니다.
     * @return ProductResponseDto 생성된 상품의 정보가 담긴 DTO입니다.
     */
    @PostMapping("/products") // POST 메서드로 "/api/products" 경로에 매핑됩니다.
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto) {
        // 서비스 계층의 createProduct 메서드를 호출하여 상품을 생성하고, 결과를 반환합니다.
        return productService.createProduct(requestDto);
    }
}
