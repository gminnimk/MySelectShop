package com.sparta.myselectshop.entity;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.naver.dto.ItemDto;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ✅ Product 클래스는 상품 정보를 데이터베이스와 상호작용하는 엔티티 클래스입니다.
 *
 *    ➡️ JPA (Java Persistence API)를 사용하여 데이터베이스의 `product` 테이블과 매핑됩니다.
 */
@Entity // JPA 엔티티로 선언, 데이터베이스 테이블과 매핑됩니다.
@Getter
@Setter
@Table(name = "product") // 이 엔티티가 매핑될 테이블의 이름을 지정합니다.
@NoArgsConstructor // Lombok 어노테이션으로 기본 생성자를 자동으로 생성합니다.
public class Product extends Timestamped {

    /**
     * ✅ 상품의 고유 ID입니다.
     *
     *    ➡️ 데이터베이스에서 자동으로 생성되는 기본 키입니다.
     */
    @Id // 이 필드가 엔티티의 기본 키임을 나타냅니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성 전략을 지정합니다. 데이터베이스에서 자동으로 값을 생성합니다.
    private Long id;

    /**
     * ✅ 상품의 제목입니다.
     *
     *    ➡️ 데이터베이스에서 null 값을 허용하지 않으며, 상품의 이름을 나타냅니다.
     */
    @Column(nullable = false) // 이 필드가 데이터베이스의 열로 매핑됨을 나타내며, null 값을 허용하지 않음을 의미합니다.
    private String title;

    /**
     * ✅ 상품의 이미지 URL입니다.
     *
     *    ➡️ 데이터베이스에서 null 값을 허용하지 않으며, 상품의 이미지를 식별하는 URL을 나타냅니다.
     */
    @Column(nullable = false)
    private String image;

    /**
     * ✅ 상품의 링크 URL입니다.
     *
     *    ➡️ 데이터베이스에서 null 값을 허용하지 않으며, 상품의 상세 페이지 또는 판매 페이지를 링크합니다.
     */
    @Column(nullable = false)
    private String link;

    /**
     * ✅ 상품의 최저가입니다.
     *
     *    ➡️ 데이터베이스에서 null 값을 허용하지 않으며, 상품의 최저 가격 정보를 저장합니다.
     */
    @Column(nullable = false)
    private int lprice;

    /**
     * ✅ 사용자가 설정한 가격입니다.
     *
     *    ➡️ 데이터베이스에서 null 값을 허용하지 않으며, 사용자가 지정한 가격 정보를 저장합니다.
     */
    @Column(nullable = false)
    private int myprice;

    /**
     * ✅ 이 상품을 등록한 사용자 정보입니다.
     *
     *    ➡️ 데이터베이스에서 null 값을 허용하지 않으며, `user` 테이블과 연관 관계를 가집니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * ✅ 상품과 연관된 폴더 목록입니다.
     *
     *    ➡️ 상품이 포함된 폴더들을 리스트 형태로 저장합니다.
     */
    @OneToMany(mappedBy = "product") // 일대다 관계를 나타내며, "product" 필드에 의해 매핑됩니다.
    private List<ProductFolder> productFolderList = new ArrayList<>();

    /**
     * ✅ ProductRequestDto를 기반으로 Product 객체를 초기화합니다.
     *
     *    ➡️ DTO에서 전달받은 상품 정보를 엔티티 객체로 변환하여 데이터베이스에 저장할 준비를 합니다.
     *
     * @param requestDto 상품 정보가 포함된 DTO 객체입니다.
     * @param user 상품을 등록한 사용자 정보입니다.
     */
    public Product(ProductRequestDto requestDto, User user) {
        this.title = requestDto.getTitle(); // DTO에서 제목을 가져와 설정합니다.
        this.image = requestDto.getImage(); // DTO에서 이미지 URL을 가져와 설정합니다.
        this.link = requestDto.getLink(); // DTO에서 링크 URL을 가져와 설정합니다.
        this.lprice = requestDto.getLprice(); // DTO에서 최저가를 가져와 설정합니다.
        this.user = user; // 상품을 등록한 사용자 정보를 설정합니다.
    }

    /**
     * ✅ 사용자가 설정한 가격을 업데이트합니다.
     *
     *    ➡️ ProductMypriceRequestDto를 사용하여 `myprice` 필드를 업데이트합니다.
     *
     * @param requestDto 사용자 설정 가격이 포함된 DTO 객체입니다.
     */
    public void update(ProductMypriceRequestDto requestDto) {
        this.myprice = requestDto.getMyprice(); // DTO에서 설정된 가격을 가져와 `myprice` 필드를 업데이트합니다.
    }

    /**
     * ✅ ItemDto를 사용하여 현재 객체의 lprice 값을 업데이트합니다.
     *
     *    ➡️ 주어진 ItemDto에서 가격 정보를 가져와서 현재 객체의 lprice 필드를 업데이트합니다.
     *
     * @param itemDto 업데이트에 사용할 가격 정보가 포함된 ItemDto 객체입니다.
     */
    public void updateByItemDto(ItemDto itemDto) {
        this.lprice = itemDto.getLprice(); // ItemDto에서 최저가를 가져와 `lprice` 필드를 업데이트합니다.
    }
}
