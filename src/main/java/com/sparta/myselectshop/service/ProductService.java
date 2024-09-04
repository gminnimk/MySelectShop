package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.entity.UserRoleEnum;
import com.sparta.myselectshop.naver.dto.ItemDto;
import com.sparta.myselectshop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * ✅ ProductService 클래스는 상품과 관련된 비즈니스 로직을 처리하는 서비스 클래스입니다.
 *
 *    ➡️ 상품 생성, 조회, 업데이트 등의 작업을 수행합니다.
 *    ➡️ ProductRepository를 통해 데이터베이스와 상호작용합니다.
 */
@Service // 이 클래스가 Spring의 서비스 컴포넌트임을 나타냅니다.
@RequiredArgsConstructor // Lombok의 @RequiredArgsConstructor 어노테이션으로, final 필드에 대한 생성자를 자동으로 생성합니다.
public class ProductService {

    private final ProductRepository productRepository; // 상품 정보를 처리하는 리포지토리

    public static final int MIN_MY_PRICE = 100; // 사용자가 설정할 수 있는 최소 가격

    /**
     * ✅ 새로운 상품을 생성합니다.
     *
     *    ➡️ 클라이언트로부터 받은 요청 데이터(ProductRequestDto)를 바탕으로 상품을 생성하고, 데이터베이스에 저장합니다.
     *        ➡️ 저장된 상품을 ProductResponseDto 형식으로 반환합니다.
     *
     * @param requestDto 상품 생성에 필요한 요청 데이터를 담고 있는 DTO입니다.
     * @param user 상품을 생성한 사용자 객체입니다.
     * @return ProductResponseDto 생성된 상품의 정보를 포함하는 DTO입니다.
     */
    public ProductResponseDto createProduct(ProductRequestDto requestDto, User user) {
        // 요청 데이터로부터 Product 엔티티를 생성합니다.
        Product product = new Product(requestDto, user);

        // 생성된 Product 엔티티를 데이터베이스에 저장합니다.
        product = productRepository.save(product);

        // 저장된 상품 정보를 기반으로 ProductResponseDto를 생성하여 반환합니다.
        return new ProductResponseDto(product);
    }

    /**
     * ✅ 주어진 상품 ID에 해당하는 상품의 사용자 설정 가격을 업데이트합니다.
     *
     *    ➡️ 요청 본문에서 전달받은 `ProductMypriceRequestDto` 객체의 가격이 유효한지 확인한 후,
     *        해당 상품을 데이터베이스에서 조회하여 가격을 업데이트합니다.
     *        ➡️ 업데이트된 상품 정보를 `ProductResponseDto` 형식으로 반환합니다.
     *
     * @param id 업데이트할 상품의 고유 ID입니다.
     * @param requestDto 상품의 사용자 설정 가격이 포함된 DTO 객체입니다.
     * @return ProductResponseDto 업데이트된 상품 정보를 포함하는 DTO 객체입니다.
     * @throws IllegalAccessException 요청 본문에서 전달받은 가격이 최소 가격 기준을 충족하지 않을 경우 발생하는 예외입니다.
     */
    @Transactional // 트랜잭션 처리를 위한 어노테이션입니다.
    public ProductResponseDto updateProduct(Long id, ProductMypriceRequestDto requestDto) throws IllegalAccessException {
        int myprice = requestDto.getMyprice(); // 요청 본문에서 전달받은 사용자 설정 가격

        // 사용자 설정 가격이 최소 가격 기준을 충족하지 않을 경우 예외를 던집니다.
        if (myprice < MIN_MY_PRICE) {
            throw new IllegalAccessException("유효하지 않은 관심 가격입니다. 최소 " + MIN_MY_PRICE + "원 이상으로 설정해 주세요.");
        }

        // 데이터베이스에서 상품을 조회하고, 없을 경우 예외를 던집니다.
        Product product = productRepository.findById(id).orElseThrow(() ->
            new NullPointerException("해당 상품을 찾을 수 없습니다.")
        );

        // 조회된 상품의 가격을 업데이트합니다.
        product.update(requestDto);

        // 업데이트된 상품 정보를 기반으로 ProductResponseDto를 생성하여 반환합니다.
        return new ProductResponseDto(product);
    }

    /**
     * ✅ 데이터베이스에서 현재 사용자가 등록한 모든 상품 정보를 조회하여 페이지네이션된 DTO 리스트로 변환합니다.
     *
     *    ➡️ 데이터베이스에서 사용자가 등록한 상품을 조회하고, 각 상품을 `ProductResponseDto`로 변환하여 반환합니다.
     *
     * @param user 상품을 조회할 사용자 객체입니다.
     * @param page 조회할 페이지 번호입니다.
     * @param size 한 페이지에 표시할 항목 수입니다.
     * @param sortBy 정렬 기준이 되는 필드명입니다.
     * @param isAsc 오름차순 정렬 여부를 나타냅니다.
     * @return Page<ProductResponseDto> 사용자가 등록한 모든 상품 정보를 담고 있는 페이지네이션된 DTO 객체 리스트입니다.
     */
    public Page<ProductResponseDto> getProducts(User user, int page, int size, String sortBy, boolean isAsc) {
        // 정렬 방식을 설정합니다.
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // 사용자의 역할에 따라 다른 조회 로직을 적용합니다.
        UserRoleEnum userRoleEnum = user.getRole();
        Page<Product> productList;

        // 일반 사용자는 자신의 상품만 조회하고, 관리자는 모든 상품을 조회합니다.
        if (userRoleEnum == UserRoleEnum.USER) {
            productList = productRepository.findAllByUser(user, pageable);
        } else {
            productList = productRepository.findAll(pageable);
        }

        // 조회한 상품 목록을 ProductResponseDto로 변환하여 반환합니다.
        return productList.map(ProductResponseDto::new);
    }

    /**
     * ✅ 주어진 상품 ID와 ItemDto를 사용하여 상품 정보를 업데이트합니다.
     *
     *    ➡️ 주어진 ID로 상품을 조회한 후, 해당 상품이 존재하지 않으면 예외를 발생시킵니다.
     *        ➡️ 상품이 존재하면, ItemDto를 사용하여 상품의 정보를 업데이트합니다.
     *
     * @param id 업데이트할 상품의 ID입니다.
     * @param itemDto 상품 정보를 업데이트하는 데 사용할 ItemDto 객체입니다.
     * @throws NullPointerException 상품이 존재하지 않을 경우 발생하는 예외입니다.
     */
    @Transactional // 트랜잭션 처리를 위한 어노테이션입니다.
    public void updateBySearch(Long id, ItemDto itemDto) {
        // 주어진 ID로 상품을 조회하고, 상품이 존재하지 않으면 예외를 발생시킵니다.
        Product product = productRepository.findById(id).orElseThrow(() ->
            new NullPointerException("해당 상품은 존재하지 않습니다.")
        );

        // 조회된 상품의 정보를 ItemDto를 사용하여 업데이트합니다.
        product.updateByItemDto(itemDto);
    }

    /**
     * ✅ 데이터베이스에서 모든 상품 정보를 조회하여 DTO 리스트로 변환합니다.
     *
     *    ➡️ 데이터베이스에서 모든 상품을 조회하고, 각 상품을 `ProductResponseDto`로 변환한 후, 변환된 DTO 객체들을 리스트로 반환합니다.
     *
     * @return List<ProductResponseDto> 모든 상품 정보를 담고 있는 `ProductResponseDto` 객체 리스트입니다.
     */
    public List<ProductResponseDto> getAllProducts() {
        // 데이터베이스에서 모든 상품을 조회합니다.
        List<Product> productList = productRepository.findAll();

        // 조회한 상품을 DTO 리스트로 변환하기 위한 빈 리스트를 생성합니다.
        List<ProductResponseDto> responseDtoList = new ArrayList<>();

        // 조회한 각 상품을 ProductResponseDto로 변환하여 리스트에 추가합니다.
        for (Product product : productList) {
            responseDtoList.add(new ProductResponseDto(product));
        }
        // 변환된 DTO 리스트를 반환합니다.
        return responseDtoList;
    }
}