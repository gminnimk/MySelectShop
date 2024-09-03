package com.sparta.myselectshop.dto;

import com.sparta.myselectshop.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ✅ ProductResponseDto 클래스는 상품의 응답 정보를 담기 위한 데이터 전송 객체입니다.
 *
 *    ➡️ 서버가 클라이언트에게 상품 정보를 전달할 때 사용됩니다.
 */
@Getter
@NoArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String title;
    private String link;
    private String image;
    private int lprice;
    private int myprice;

    /**
     * Product 엔티티 객체를 기반으로 ProductResponseDto를 생성합니다.
     *
     * @param product Product 엔티티 객체
     * 엔티티 객체의 필드를 이용하여 DTO를 초기화합니다.
     */
    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.link = product.getLink();
        this.image = product.getImage();
        this.lprice = product.getLprice();
        this.myprice = product.getMyprice();
    }
}