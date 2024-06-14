package com.sparta.myselectshop.controller;


import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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



    // @PutMapping("/products/{id}"): 이 애노테이션은 HTTP PUT 요청을 처리하는 메서드를 정의합니다. /products/{id} 경로는 업데이트할 제품의 ID를 URL 경로 변수로 받습니다.
    // public ProductResponseDto updateProduct(...): 이 메서드는 ProductResponseDto 객체를 반환합니다. 이는 클라이언트에게 업데이트된 제품 정보를 응답하기 위함입니다.

    // @PathVariable Long id: URL 경로에서 {id} 부분을 추출하여 id 변수에 할당합니다. 이 변수는 업데이트할 제품의 고유 식별자입니다.
    // @RequestBody ProductMypriceRequestDto requestDto: 요청 본문에서 ProductMypriceRequestDto 객체를 추출하여 requestDto 변수에 할당합니다.
    // 이 객체는 클라이언트가 보내는 업데이트할 제품의 새로운 희망 가격을 담고 있습니다.

    @PutMapping("/products/{id}")
    public ProductResponseDto updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto requestDto) throws IllegalAccessException {
        return productService.updateProduct(id, requestDto); // ProductService 클래스의 updateProduct 메서드를 호출하여 제품 정보를 업데이트합니다. 이때 제품의 ID와 새로운 희망 가격을 담고 있는 requestDto 객체를 전달합니다.
    }





    // '/products' 경로로 GET 요청이 들어왔을 때, 제품 목록을 조회하여 클라이언트에게 반환하는 컨트롤러 메서드입니다.
    //  클라이언트가 제품 목록을 요청할 때 사용됩니다. 클라이언트는 /products 경로로 GET 요청을 보내면 이 메서드가 호출되어 제품 목록을 응답으로 받아올 수 있습니다.


    // @GetMapping("/products"): 이 애노테이션은 HTTP GET 요청을 처리하는 메서드임을 나타냅니다. /products 경로로 들어오는 GET 요청을 이 메서드가 처리합니다.
    // public List<ProductResponseDto> getProducts(): 이 메서드는 List<ProductResponseDto> 객체를 반환합니다.
    // 이는 클라이언트에게 제품 목록의 각 제품 정보를 담은 DTO 객체들을 전달하기 위함입니다.

    @GetMapping("/products")
    public List<ProductResponseDto> getProducts() {


        // productService.getProducts(): ProductService 클래스의 getProducts 메서드를 호출하여 제품 목록을 가져옵니다.
        // 이 메서드는 실제로 데이터베이스에서 제품을 조회하고, 각 제품을 ProductResponseDto로 변환하여 리스트로 반환합니다.
        // getProducts 메서드가 반환한 제품 목록을 그대로 클라이언트에게 반환합니다.
        return productService.getProducts();
    }
}
