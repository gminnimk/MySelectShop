package com.sparta.myselectshop.dto;

import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.entity.ProductFolder;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ✅ ProductResponseDto 클래스는 상품 정보를 클라이언트에게 전달하기 위한 데이터 전송 객체입니다.
 *
 *    ➡️ 서버에서 클라이언트로 상품 정보를 전송할 때 사용되며, 상품의 기본 정보와 관련된 폴더 정보를 포함합니다.
 */
@Getter
@NoArgsConstructor
public class ProductResponseDto {

    /**
     * ✅ 상품의 고유 ID입니다.
     *
     *    ➡️ 데이터베이스에서 생성된 기본 키로, 상품을 고유하게 식별합니다.
     */
    private Long id;

    /**
     * ✅ 상품의 제목입니다.
     *
     *    ➡️ 상품의 이름 또는 제목을 나타냅니다.
     */
    private String title;

    /**
     * ✅ 상품의 링크 URL입니다.
     *
     *    ➡️ 상품의 상세 페이지 또는 판매 페이지를 링크하는 URL입니다.
     */
    private String link;

    /**
     * ✅ 상품의 이미지 URL입니다.
     *
     *    ➡️ 상품의 이미지를 식별하는 URL입니다.
     */
    private String image;

    /**
     * ✅ 상품의 최저가입니다.
     *
     *    ➡️ 상품의 최저 가격 정보를 나타냅니다.
     */
    private int lprice;

    /**
     * ✅ 사용자가 설정한 가격입니다.
     *
     *    ➡️ 사용자가 지정한 상품의 가격입니다.
     */
    private int myprice;

    /**
     * ✅ 상품과 연관된 폴더 목록입니다.
     *
     *    ➡️ 상품이 포함된 폴더들의 정보를 담고 있으며, 각 폴더는 FolderResponseDto 객체로 표현됩니다.
     */
    private List<FolderResponseDto> productFolderList = new ArrayList<>();

    /**
     * ✅ Product 엔티티 객체를 기반으로 ProductResponseDto를 생성합니다.
     *
     *    ➡️ Product 엔티티 객체의 필드 값을 사용하여 DTO를 초기화합니다.
     *    ➡️ 연관된 ProductFolder 목록을 순회하여 각 폴더를 FolderResponseDto로 변환하여 리스트에 추가합니다.
     *
     * @param product Product 엔티티 객체입니다. DTO의 필드 값은 이 엔티티 객체에서 가져옵니다.
     */
    public ProductResponseDto(Product product) {
        this.id = product.getId(); // 엔티티의 ID를 설정합니다.
        this.title = product.getTitle(); // 엔티티의 제목을 설정합니다.
        this.link = product.getLink(); // 엔티티의 링크 URL을 설정합니다.
        this.image = product.getImage(); // 엔티티의 이미지 URL을 설정합니다.
        this.lprice = product.getLprice(); // 엔티티의 최저가를 설정합니다.
        this.myprice = product.getMyprice(); // 엔티티의 사용자가 설정한 가격을 설정합니다.

        // 연관된 ProductFolder 리스트를 FolderResponseDto 리스트로 변환하여 설정합니다.
        for (ProductFolder productFolder : product.getProductFolderList()) {
            productFolderList.add(new FolderResponseDto(productFolder.getFolder()));
        }
    }
}
