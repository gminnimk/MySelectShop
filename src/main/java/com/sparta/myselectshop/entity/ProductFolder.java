package com.sparta.myselectshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ✅ ProductFolder 엔티티 클래스는 상품과 폴더 간의 다대다 관계를 나타냅니다.
 *
 *    ➡️ 이 클래스는 특정 상품이 특정 폴더에 저장된 관계를 표현하며, 데이터베이스에 저장됩니다.
 */
@Entity // 이 클래스가 JPA 엔티티임을 나타냅니다.
@Getter
@Setter
@NoArgsConstructor // Lombok 어노테이션으로 기본 생성자를 자동으로 생성합니다.
@Table(name = "product_folder") // 데이터베이스 테이블 이름을 "product_folder"로 설정합니다.
public class ProductFolder {

    /**
     * ✅ 고유 ID입니다.
     *
     *    ➡️ 데이터베이스에서 자동으로 생성되는 기본 키입니다.
     */
    @Id // 이 필드가 엔티티의 기본 키임을 나타냅니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성 전략을 데이터베이스의 자동 증가로 설정합니다.
    private Long id;

    /**
     * ✅ 상품 엔티티입니다.
     *
     *    ➡️ 이 ProductFolder가 참조하는 상품을 나타냅니다.
     *    ➡️ 지연 로딩을 설정하여 필요 시에만 로드됩니다.
     */
    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계를 나타내며, 지연 로딩을 설정합니다.
    @JoinColumn(name = "product_id", nullable = false) // 외래 키 컬럼으로 매핑되며, 빈 값을 허용하지 않습니다.
    private Product product;

    /**
     * ✅ 폴더 엔티티입니다.
     *
     *    ➡️ 이 ProductFolder가 참조하는 폴더를 나타냅니다.
     *    ➡️ 지연 로딩을 설정하여 필요 시에만 로드됩니다.
     */
    @ManyToOne(fetch = FetchType.LAZY) // 다대일 관계를 나타내며, 지연 로딩을 설정합니다.
    @JoinColumn(name = "folder_id", nullable = false) // 외래 키 컬럼으로 매핑되며, 빈 값을 허용하지 않습니다.
    private Folder folder;

    /**
     * ✅ 상품과 폴더를 초기화하는 생성자입니다.
     *
     *    ➡️ 상품과 폴더 간의 관계를 설정하기 위한 생성자입니다.
     *
     * @param product 상품 엔티티
     * @param folder  폴더 엔티티
     */
    public ProductFolder(Product product, Folder folder) {
        this.product = product;
        this.folder = folder;
    }
}
