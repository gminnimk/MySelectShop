package com.sparta.myselectshop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.repository.FolderRepository;
import com.sparta.myselectshop.repository.ProductFolderRepository;
import com.sparta.myselectshop.repository.ProductRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class) // @Mock 사용을 위해 Mockito 확장을 적용합니다.
class ProductServiceTest {

    /**
     * ✅ @Mock 어노테이션으로 가짜 객체(Mock 객체)를 생성합니다.
     *
     *      ➡️ ProductRepository, FolderRepository, ProductFolderRepository는 실제로 구현되지 않고
     *          테스트에서 가짜로 동작하도록 설정됩니다.
     */
    @Mock
    ProductRepository productRepository;

    @Mock
    FolderRepository folderRepository;

    @Mock
    ProductFolderRepository productFolderRepository;

    /**
     * ✅ 관심 상품의 희망가를 최저가 이상으로 설정하는 테스트입니다.
     *
     *      ➡️ ProductService의 updateProduct() 메서드가 정상적으로 동작하여, 희망가가 성공적으로 업데이트되는지 확인합니다.
     */
    @Test
    @DisplayName("관심 상품 희망가 - 최저가 이상으로 변경")
    void test1() throws IllegalAccessException {
        // given (테스트에 필요한 데이터와 상황을 준비하는 단계)
        Long productId = 100L; // 상품 ID
        int myprice = ProductService.MIN_MY_PRICE + 3_000_000; // 희망 가격 (최저가보다 높은 값)

        // 희망 가격을 담은 DTO 생성
        ProductMypriceRequestDto requestMyPriceDto = new ProductMypriceRequestDto();
        requestMyPriceDto.setMyprice(myprice);

        // 사용자 및 상품 정보를 설정
        User user = new User();
        ProductRequestDto requestProductDto = new ProductRequestDto(
            "Apple <b>맥북</b> <b>프로</b> 16형 2021년 <b>M1</b> Max 10코어 실버 (MK1H3KH/A)",
            "https://shopping-phinf.pstatic.net/main_2941337/29413376619.20220705152340.jpg",
            "https://search.shopping.naver.com/gate.nhn?id=29413376619",
            3515000
        );

        // 상품 객체 생성
        Product product = new Product(requestProductDto, user);

        // ProductService 인스턴스 생성
        ProductService productService = new ProductService(productRepository, folderRepository, productFolderRepository);

        // 가짜 productRepository에서 특정 productId로 상품을 찾을 때 해당 product를 반환하도록 설정
        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // when (테스트하려는 동작 실행)
        ProductResponseDto result = productService.updateProduct(productId, requestMyPriceDto);

        // then (결과를 검증)
        assertEquals(myprice, result.getMyprice()); // 희망 가격이 정상적으로 변경되었는지 확인
    }

    /**
     * ✅ 관심 상품의 희망가를 최저가 미만으로 설정하는 테스트입니다.
     *
     *      ➡️ ProductService의 updateProduct() 메서드가 최저가 미만일 때, IllegalArgumentException을 던지는지 확인합니다.
     */
    @Test
    @DisplayName("관심 상품 희망가 - 최저가 미만으로 변경")
    void test2() {
        // given (테스트에 필요한 데이터와 상황을 준비하는 단계)
        Long productId = 200L; // 상품 ID
        int myprice = ProductService.MIN_MY_PRICE - 50; // 희망 가격 (최저가보다 낮은 값)

        // 희망 가격을 담은 DTO 생성
        ProductMypriceRequestDto requestMyPriceDto = new ProductMypriceRequestDto();
        requestMyPriceDto.setMyprice(myprice);

        // ProductService 인스턴스 생성
        ProductService productService = new ProductService(productRepository, folderRepository, productFolderRepository);

        // when (테스트하려는 동작 실행)
        // 희망가가 최저가 미만일 경우 예외가 발생해야 함
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(productId, requestMyPriceDto);
        });

        // then (결과를 검증)
        // 예외 메시지가 예상대로 나오는지 확인
        assertEquals(
            "유효하지 않은 관심 가격입니다. 최소 " + ProductService.MIN_MY_PRICE + " 원 이상으로 설정해 주세요.",
            exception.getMessage()
        );
    }
}