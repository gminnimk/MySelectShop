package com.sparta.myselectshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ✅ ProductRequestDto 클래스는 상품의 요청 정보를 담기 위한 데이터 전송 객체입니다.
 *
 *    ➡️ 클라이언트로부터 상품에 대한 정보를 수집하고 서버로 전송하는 데 사용됩니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
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