package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT: 이 설정은 테스트 실행 시 내장 웹 서버를 랜덤 포트로 실행하게 만듭니다.
// 이를 통해 실제 애플리케이션처럼 서버가 실행되며, 테스트에서 이 서버로 요청을 보낼 수 있습니다.
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 테스트 인스턴스의 생성 단위를 클래스로 설정하여 클래스 당 한 번 생성되도록 함
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // 테스트 메서드 실행 순서를 지정할 수 있도록 설정
class ProductServiceIntegrationTest {

    // 서비스 클래스와 리포지토리를 자동으로 주입받음
    @Autowired
    ProductService productService;
    @Autowired
    UserRepository userRepository;

    // 테스트에서 사용할 변수 선언
    User user;
    ProductResponseDto createdProduct = null; // 생성된 관심상품을 저장할 변수
    int updatedMyPrice = -1; // 업데이트된 희망 가격을 저장할 변수

    /**
     * ✅ 관심상품을 신규로 등록하는 테스트 메서드
     *
     *      ➡️ ProductService의 createProduct 메서드를 통해 상품을 등록하고, 그 결과를 검증합니다.
     */
    @Test
    @Order(1) // 첫 번째로 실행될 테스트
    @DisplayName("신규 관심상품 등록")
    void test1() {
        // given (테스트에 필요한 데이터 준비)
        String title = "Apple <b>에어팟</b> 2세대 유선충전 모델 (MV7N2KH/A)";
        String imageUrl = "https://shopping-phinf.pstatic.net/main_1862208/18622086330.20200831140839.jpg";
        String linkUrl = "https://search.shopping.naver.com/gate.nhn?id=18622086330";
        int lPrice = 173900;
        ProductRequestDto requestDto = new ProductRequestDto(title, imageUrl, linkUrl, lPrice);
        user = userRepository.findById(1L).orElse(null); // 테스트용 유저를 가져옴

        // when (테스트할 동작 실행)
        ProductResponseDto product = productService.createProduct(requestDto, user);

        // then (결과 검증)
        assertNotNull(product.getId()); // 생성된 상품의 ID가 null이 아닌지 확인
        assertEquals(title, product.getTitle()); // 상품 제목이 일치하는지 확인
        assertEquals(imageUrl, product.getImage()); // 이미지 URL이 일치하는지 확인
        assertEquals(linkUrl, product.getLink()); // 링크가 일치하는지 확인
        assertEquals(lPrice, product.getLprice()); // 최저가가 일치하는지 확인
        assertEquals(0, product.getMyprice()); // 기본 희망가가 0인지 확인
        createdProduct = product; // 생성된 상품을 저장하여 다른 테스트에서 사용
    }

    /**
     * ✅ 신규로 등록된 관심상품의 희망 최저가를 변경하는 테스트 메서드
     *
     *      ➡️ ProductService의 updateProduct 메서드를 통해 상품의 희망 가격을 변경하고, 그 결과를 검증합니다.
     */
    @Test
    @Order(2) // 두 번째로 실행될 테스트
    @DisplayName("신규 등록된 관심상품의 희망 최저가 변경")
    void test2() throws IllegalAccessException {
        // given (변경할 상품 ID와 새로운 희망 가격을 준비)
        Long productId = this.createdProduct.getId(); // 생성된 상품의 ID 가져오기
        int myPrice = 173000; // 설정할 희망 가격
        ProductMypriceRequestDto requestDto = new ProductMypriceRequestDto();
        requestDto.setMyprice(myPrice);

        // when (테스트할 동작 실행)
        ProductResponseDto product = productService.updateProduct(productId, requestDto);

        // then (결과 검증)
        assertNotNull(product.getId()); // 상품 ID가 존재하는지 확인
        assertEquals(this.createdProduct.getTitle(), product.getTitle()); // 상품 제목이 변경 전과 일치하는지 확인
        assertEquals(this.createdProduct.getImage(), product.getImage()); // 이미지가 변경 전과 일치하는지 확인
        assertEquals(this.createdProduct.getLink(), product.getLink()); // 링크가 변경 전과 일치하는지 확인
        assertEquals(this.createdProduct.getLprice(), product.getLprice()); // 최저가가 변경 전과 일치하는지 확인
        assertEquals(myPrice, product.getMyprice()); // 희망 가격이 정상적으로 변경되었는지 확인
        this.updatedMyPrice = myPrice; // 변경된 희망 가격을 저장하여 다음 테스트에서 사용
    }

    /**
     * ✅ 회원이 등록한 모든 관심상품을 조회하는 테스트 메서드
     *
     *      ➡️ ProductService의 getProducts 메서드를 통해 회원이 등록한 상품 목록을 조회하고, 그 결과를 검증합니다.
     */
    @Test
    @Order(3) // 세 번째로 실행될 테스트
    @DisplayName("회원이 등록한 모든 관심상품 조회")
    void test3() {
        // given (특별히 필요 없음)

        // when (회원의 모든 관심상품을 조회)
        Page<ProductResponseDto> productList = productService.getProducts(user, 0, 10, "id", false);

        // then (결과 검증)
        // 1. 전체 상품 목록에서 테스트에 의해 생성된 상품을 ID로 찾기
        Long createdProductId = this.createdProduct.getId();
        ProductResponseDto foundProduct = productList.stream()
            .filter(product -> product.getId().equals(createdProductId))
            .findFirst()
            .orElse(null);

        // 2. Order(1)에서 생성된 상품과 일치하는지 확인
        assertNotNull(foundProduct); // 생성된 상품이 목록에 있는지 확인
        assertEquals(this.createdProduct.getId(), foundProduct.getId()); // ID가 일치하는지 확인
        assertEquals(this.createdProduct.getTitle(), foundProduct.getTitle()); // 제목이 일치하는지 확인
        assertEquals(this.createdProduct.getImage(), foundProduct.getImage()); // 이미지가 일치하는지 확인
        assertEquals(this.createdProduct.getLink(), foundProduct.getLink()); // 링크가 일치하는지 확인
        assertEquals(this.createdProduct.getLprice(), foundProduct.getLprice()); // 최저가가 일치하는지 확인

        // 3. Order(2)에서 변경된 희망 가격이 정상적으로 반영되었는지 확인
        assertEquals(this.updatedMyPrice, foundProduct.getMyprice()); // 희망 가격이 일치하는지 확인
    }
}