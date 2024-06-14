package com.sparta.myselectshop.dto;

import com.sparta.myselectshop.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;


// 이 클래스는 서버에서 클라이언트로 전송되는 제품 정보를 캡슐화합니다.


/*
ProductResponseDto 클래스는 서버가 클라이언트에게 제품 정보를 응답할 때 사용되는 DTO입니다.
이 클래스는 제품의 ID, 이름, 구매 링크, 이미지 URL, 최저가, 희망 가격 정보를 담고 있습니다.
Lombok의 @Getter 애노테이션을 통해 모든 필드에 대한 getter 메서드를 자동으로 생성하며, @NoArgsConstructor 애노테이션을 통해 기본 생성자를 자동으로 생성합니다.

또한, Product 엔티티 객체를 매개변수로 받는 생성자를 통해 Product 객체의 데이터를 ProductResponseDto 객체로 쉽게 변환할 수 있습니다.
이는 서비스 계층이나 컨트롤러에서 Product 객체를 ProductResponseDto로 변환하여 클라이언트에게 응답할 때 유용합니다.
 */


@Getter // Lombok 애노테이션으로, 이 클래스의 모든 필드에 대한 getter 메서드를 자동으로 생성해 줍니다.
@NoArgsConstructor // Lombok 애노테이션으로, 파라미터가 없는 기본 생성자를 자동으로 생성해 줍니다.
public class ProductResponseDto {
    private Long id; // 제품의 고유 ID를 저장합니다.
    private String title; // 제품의 이름을 저장합니다.
    private String link; // 제품의 구매 링크 URL을 저장합니다.
    private String image; // 제품의 이미지 URL을 저장합니다.
    private int lprice; // 제품의 최저가를 저장합니다.
    private int myprice; // 사용자가 설정한 희망 가격을 저장합니다.




    // Product 객체를 매개변수로 받는 생성자입니다. Product 엔티티 객체의 필드 값을 ProductResponseDto 객체의 필드에 복사합니다.
    // 예를 들어, product.getId()를 통해 Product 객체의 ID 값을 가져와 this.id에 할당합니다.

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.link = product.getLink();
        this.image = product.getImage();
        this.lprice = product.getLprice();
        this.myprice = product.getMyprice();
    }
}