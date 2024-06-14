package com.sparta.myselectshop.service;


import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.naver.dto.ItemDto;
import com.sparta.myselectshop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


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


    public static final int MIN_MY_PRICE = 100;

    public ProductResponseDto createProduct(ProductRequestDto requestDto, User user) { // 클라이언트로부터 전달받은 ProductRequestDto 객체를 바탕으로 새로운 제품을 생성하는 메서드입니다.
        Product product = productRepository.save(new Product(requestDto, user)); // ProductRequestDto 객체를 사용해 새로운 Product 객체를 생성하고, 이를 productRepository를 통해 데이터베이스에 저장합니다.
        return new ProductResponseDto(product); // 저장된 Product 객체를 ProductResponseDto로 변환하여 반환합니다. 이는 클라이언트에게 저장된 제품 정보를 응답하기 위함입니다.
    }

    @Transactional // 이 애노테이션은 메서드가 하나의 트랜잭션으로 실행됨을 나타냅니다. 메서드 내에서 일어나는 모든 데이터베이스 작업은 하나의 단위로 처리됩니다.
    public ProductResponseDto updateProduct(Long id, ProductMypriceRequestDto requestDto) throws IllegalAccessException {

        // int myprice = requestDto.getMyprice();: 클라이언트가 전달한 새로운 희망 가격을 가져옵니다.
        // if (myprice < MIN_MY_PRICE) { ... }: 가져온 희망 가격이 설정한 최소 허용 가격(MIN_MY_PRICE)보다 낮으면 예외를 발생시킵니다.
        int myprice = requestDto.getMyprice();
        if (myprice < MIN_MY_PRICE) {
            throw new IllegalAccessException("유효하지 않은 관심 가격입니다. 최소 " + MIN_MY_PRICE + "원 이상으로 설정해 주세요.");
        }


        // productRepository.findById(id): 주어진 ID에 해당하는 상품을 데이터베이스에서 조회합니다.
        // .orElseThrow(...): 조회한 상품이 없으면 NullPointerException을 발생시킵니다.
        // product.update(requestDto): 조회한 상품 객체의 정보를 클라이언트가 전달한 희망 가격으로 업데이트합니다.

        //  제품의 희망 가격을 업데이트하는 REST API 엔드포인트에서 사용될 수 있습니다.
        //  클라이언트는 PUT 요청을 통해 특정 상품의 희망 가격을 변경하고, 이 메서드는 그 변경을 처리하여 응답합니다.
        Product product = productRepository.findById(id).orElseThrow(() ->
                new NullPointerException("해당 상품을 찾을 수 없습니다.")
        );

        product.update(requestDto);

        return new ProductResponseDto(product); // 업데이트된 상품 정보를 담고 있는 ProductResponseDto 객체를 생성하여 반환합니다.
    }


    //  제품 목록을 조회하여 각 제품을 ProductResponseDto로 변환한 후, 클라이언트에게 반환하는 메서드입니다.
    // 주로 제품 목록을 가져와서 화면에 표시하거나 API를 통해 클라이언트에게 제공할 때 사용됩니다.
    // 클라이언트는 이 메서드를 호출하여 모든 제품의 정보를 받아와 활용할 수 있습니다.


    // public: 이 메서드는 공개(public)되어 있어 다른 클래스에서 호출할 수 있습니다.
    // List<ProductResponseDto>: ProductResponseDto 객체들을 담고 있는 리스트를 반환합니다. 각 객체는 제품의 정보를 클라이언트에게 전달하는 데 사용됩니다.
    // getProducts(): 메서드 이름은 getProducts로, 클라이언트가 제품 목록을 가져오는 기능을 수행합니다.
    public List<ProductResponseDto> getProducts(User user) {


        // productRepository.findAll(): 데이터베이스에 저장된 모든 제품을 조회하여 productList에 저장합니다.
        // List<ProductResponseDto> responseDtoList = new ArrayList<>();: ProductResponseDto 객체들을 담을 새로운 리스트 responseDtoList를 생성합니다.
        List<Product> productList = productRepository.findAllByUser(user);
        List<ProductResponseDto> responseDtoList = new ArrayList<>();


        // for (Product product : productList) { ... }: productList에 있는 각 Product 객체를 반복하면서,
        // new ProductResponseDto(product): 각 Product 객체를 ProductResponseDto로 변환하여 responseDtoList에 추가합니다.
        for (Product product : productList) {
            responseDtoList.add(new ProductResponseDto(product));
        }


        // ProductResponseDto 객체들을 담고 있는 responseDtoList를 반환합니다.
        return responseDtoList;
    }

    @Transactional
    public void updateBySearch(Long id, ItemDto itemDto) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new NullPointerException("해당 상품은 존재하지 않습니다.")
        );
        product.updateByItemDto(itemDto);
    }

    public List<ProductResponseDto> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        List<ProductResponseDto> responseDtoList = new ArrayList<>();

        for (Product product : productList) {
            responseDtoList.add(new ProductResponseDto(product));
        }

        return responseDtoList;

    }
}


