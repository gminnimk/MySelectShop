package com.sparta.myselectshop.entity;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.naver.dto.ItemDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


// 이 클래스는 데이터베이스 테이블에 매핑되어, 제품 정보를 저장하는 역할을 합니다.


/*
Product 클래스는 JPA 엔티티로, 데이터베이스의 product 테이블에 매핑됩니다.
이 클래스는 제품의 ID, 이름, 이미지 URL, 구매 링크, 최저가, 희망 가격 정보를 필드로 가지고 있습니다.
Lombok의 @Getter와 @Setter 애노테이션을 통해 모든 필드에 대한 getter와 setter 메서드를 자동으로 생성하며,
@NoArgsConstructor 애노테이션을 통해 기본 생성자를 자동으로 생성합니다.


또한, ProductRequestDto 객체를 매개변수로 받는 생성자를 통해 ProductRequestDto 객체의 데이터를 사용하여 Product 객체를 초기화할 수 있습니다.
이는 주로 클라이언트로부터 받은 요청 데이터를 데이터베이스에 저장하기 위해 사용됩니다.
 */

@Entity // 이 클래스가 JPA 엔티티임을 나타냅니다, JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@Setter
@Table(name = "product") // 매핑할 테이블의 이름을 지정, 이 엔티티가 데이터베이스의 product 테이블에 매핑됨을 나타냅니다.
@NoArgsConstructor //  Lombok 애노테이션으로, 파라미터가 없는 기본 생성자를 자동으로 생성합니다.
public class Product extends Timestamped { // Product 클래스는 Timestamped 클래스를 상속받습니다.

    @Id // 이 필드가 기본 키임을 나타냅니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 값을 자동으로 생성하도록 설정합니다.
    private Long id; // 제품의 고유 ID.

    @Column(nullable = false) // 이 필드가 데이터베이스의 해당 컬럼에 매핑되며, null 값을 허용하지 않음을 나타냅니다.
    private String title; // 제품의 이름.

    @Column(nullable = false)
    private String image; // 제품의 이미지 URL.

    @Column(nullable = false)
    private String link; // 제품의 구매 링크 URL.

    @Column(nullable = false)
    private int lprice; // 제품의 최저가.

    @Column(nullable = false)
    private int myprice; // 사용자가 설정한 희망 가격.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    // public Product(ProductRequestDto requestDto): ProductRequestDto 객체를 매개변수로 받는 생성자입니다.
    // 이 생성자는 ProductRequestDto 객체의 데이터를 사용하여 Product 객체의 필드를 초기화합니다.
    // 예를 들어, requestDto.getTitle()를 통해 ProductRequestDto 객체의 제목을 가져와 this.title에 할당합니다.
    public Product(ProductRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.image = requestDto.getImage();
        this.link = requestDto.getLink();
        this.lprice = requestDto.getLprice();
        this.user = user;
    }



    // 이 메서드는 ProductMypriceRequestDto 타입의 객체를 매개변수로 받습니다. 이 객체는 클라이언트가 전달한 새로운 희망 가격을 담고 있습니다.
    // this.myprice: 현재 Product 객체의 myprice 필드를 가리킵니다.
    // requestDto.getMyprice(): ProductMypriceRequestDto 객체의 getMyprice() 메서드를 호출하여 클라이언트가 전달한 새로운 희망 가격을 가져옵니다.
    // this.myprice = requestDto.getMyprice();: 현재 Product 객체의 myprice 필드를 클라이언트가 전달한 새로운 희망 가격으로 업데이트합니다.

    public void update(ProductMypriceRequestDto requestDto) {
        this.myprice = requestDto.getMyprice();
    }


    public void updateByItemDto(ItemDto itemDto) {
        this.lprice = itemDto.getLprice();

    }
}