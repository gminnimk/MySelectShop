package com.sparta.myselectshop.controller;


import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Spring Boot 프레임워크를 사용하여 웹 애플리케이션의 일부로 작성된 RESTful 컨트롤러입니다.

/*
이 컨트롤러 클래스는 클라이언트로부터 새로운 제품 생성 요청을 받아서, 서비스 계층(ProductService)을 통해 제품을 생성하고,
그 결과를 클라이언트에게 응답합니다. ProductRequestDto는 요청 데이터, ProductResponseDto는 응답 데이터를 담는 역할을 합니다.
@RestController와 관련된 애노테이션들은 스프링이 자동으로 HTTP 요청을 처리하고 응답할 수 있게 도와줍니다.
 */


@RestController // 이 클래스가 RESTful 웹 서비스의 컨트롤러임을 나타냅니다.
@RequiredArgsConstructor //  Lombok 애노테이션으로, final로 선언된 필드를 매개변수로 받는 생성자를 자동으로 생성해 줍니다.
@RequestMapping("/api") // 이 클래스의 모든 메서드가 /api 경로 아래에서 작동함을 의미합니다.
public class ProductController {

    private final ProductService productService; // ProductService 클래스를 주입받아 사용합니다. 이 필드는 @RequiredArgsConstructor 애노테이션 덕분에 자동으로 초기화됩니다.



    // @PostMapping("/products"): HTTP POST 요청이 /api/products 경로로 들어올 때 이 메서드가 실행됩니다.
    // public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto): 이 메서드는 ProductRequestDto 객체를 요청 본문에서 받아서 ProductResponseDto 객체를 반환합니다.
    // @RequestBody: 요청 본문을 자바 객체로 변환해주는 애노테이션입니다.
    // return productService.createProduct(requestDto): productService의 createProduct 메서드를 호출하여 새로운 제품을 생성하고, 그 결과를 반환합니다.

    @PostMapping("/products")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto) {
        return productService.createProduct(requestDto);
    }
}
