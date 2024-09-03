package com.sparta.myselectshop.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * ✅ ProductMypriceRequestDto 클래스는 상품의 가격 정보를 담기 위한 데이터 전송 객체입니다.
 *
 *    ➡️ 클라이언트로부터 가격 관련 정보를 받아서 처리하는 데 사용됩니다.
 */
@Getter
@Setter
public class ProductMypriceRequestDto {

    private int myprice; // 클라이언트가 설정한 상품의 가격 정보입니다.
}