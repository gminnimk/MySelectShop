package com.sparta.myselectshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 이 DTO 클래스는 클라이언트가 서버로 전송하는 관심상품 정보를 캡슐화합니다.

/*
ProductRequestDto 클래스는 관심상품의 정보를 담는 DTO입니다. 이 클래스는 상품명(title), 썸네일 이미지 URL(image),
구매 링크 URL(link), 최저가(lprice) 정보를 필드로 가지고 있습니다.
Lombok의 @Getter 애노테이션을 통해 모든 필드에 대한 getter 메서드를 자동으로 생성하며,
@NoArgsConstructor와 @AllArgsConstructor 애노테이션을 통해 기본 생성자와 모든 필드를 매개변수로 받는 생성자를 자동으로 생성합니다.

이 클래스는 주로 클라이언트가 새로운 관심상품 정보를 서버로 전송할 때 사용됩니다. 예를 들어, 사용자가 새로운 관심상품을 등록할 때,
이 정보를 서버에 전송하면 서버는 이를 ProductRequestDto 객체로 받아서 처리할 수 있습니다.
 */

@Getter
@NoArgsConstructor // : Lombok 애노테이션으로, 파라미터가 없는 기본 생성자를 자동으로 생성해 줍니다.
@AllArgsConstructor //  Lombok 애노테이션으로, 모든 필드를 매개변수로 받는 생성자를 자동으로 생성해 줍니다. 예를 들어,
public class ProductRequestDto {
    // 관심상품명
    private String title;
    // 관심상품 썸네일 image URL
    private String image;
    // 관심상품 구매링크 URL
    private String link;
    // 관심상품의 최저가
    private int lprice;
}