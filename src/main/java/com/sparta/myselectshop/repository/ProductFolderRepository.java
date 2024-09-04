package com.sparta.myselectshop.repository;

import com.sparta.myselectshop.entity.Folder;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.entity.ProductFolder;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ✅ ProductFolderRepository는 `ProductFolder` 엔티티에 대한 데이터베이스 작업을 처리하는 JPA 리포지토리입니다.
 *
 *    ➡️ Spring Data JPA의 `JpaRepository`를 확장하여 기본적인 CRUD (Create, Read, Update, Delete) 작업을 지원합니다.
 *    ➡️ `ProductFolder` 엔티티와 `Long` 타입의 기본 키를 사용하는 리포지토리입니다.
 *    ➡️ 특정 상품과 폴더의 연관 관계를 조회하는 추가적인 메서드를 제공합니다.
 */
public interface ProductFolderRepository extends JpaRepository<ProductFolder, Long> {

    /**
     * ✅ 특정 상품과 폴더의 연관 관계를 조회하는 메서드입니다.
     *
     *    ➡️ 주어진 상품과 폴더의 조합을 기반으로 `ProductFolder` 엔티티를 조회합니다.
     *    ➡️ 상품과 폴더의 연관 관계가 존재하면 해당 연관 관계를 담은 `ProductFolder` 객체를 Optional로 반환합니다.
     *
     * @param product 조회할 상품 객체입니다.
     * @param folder 조회할 폴더 객체입니다.
     * @return Optional<ProductFolder> 상품과 폴더의 연관 관계를 담고 있는 `ProductFolder` 객체를 반환합니다. 연관 관계가 존재하지 않으면 빈 Optional을 반환합니다.
     */
    Optional<ProductFolder> findByProductAndFolder(Product product, Folder folder);
}