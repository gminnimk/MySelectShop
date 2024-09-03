package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.repository.ProductRepository;
import jakarta.transaction.Transactional;
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

    public static final int MIN_MY_PRICE = 100;

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

    /**
     * ✅ 주어진 상품 ID에 해당하는 상품의 사용자 설정 가격을 업데이트합니다.
     *
     *    ➡️ 요청 본문에서 전달받은 `ProductMypriceRequestDto` 객체의 가격이 유효한지 확인한 후,
     *       해당 상품을 데이터베이스에서 조회하여 가격을 업데이트합니다.
     *    ➡️ 업데이트된 상품 정보를 `ProductResponseDto` 형식으로 반환합니다.
     *
     * @param id 업데이트할 상품의 고유 ID입니다.
     * @param requestDto 상품의 사용자 설정 가격이 포함된 DTO 객체입니다.
     * @return ProductResponseDto 업데이트된 상품 정보를 포함하는 DTO 객체입니다.
     * @throws IllegalAccessException 요청 본문에서 전달받은 가격이 최소 가격 기준을 충족하지 않을 경우 던져집니다.
     */
    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductMypriceRequestDto requestDto) throws IllegalAccessException {
        // 요청 본문에서 전달받은 가격을 변수에 저장합니다.
        int myprice = requestDto.getMyprice();

        // 사용자 설정 가격이 최소 가격 기준에 미달할 경우 예외를 던집니다.
        if (myprice < MIN_MY_PRICE) {
            throw new IllegalAccessException("유효하지 않은 관심 가격입니다. 최소 " + MIN_MY_PRICE + "원 이상으로 설정해 주세요.");
        }

        // 데이터베이스에서 ID에 해당하는 상품을 조회합니다. 없을 경우 예외를 던집니다.
        Product product = productRepository.findById(id).orElseThrow(() ->
            new NullPointerException("해당 상품을 찾을 수 없습니다.")
        );

        // 조회된 상품 객체의 가격을 업데이트합니다.
        product.update(requestDto);

        // 업데이트된 상품을 기반으로 ProductResponseDto를 생성하여 반환합니다.
        return new ProductResponseDto(product);
    }
}