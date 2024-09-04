package com.sparta.myselectshop.repository;

import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ✅ ProductRepository 인터페이스는 Product 엔티티에 대한 CRUD (생성, 조회, 업데이트, 삭제) 작업을 처리하는 리포지토리입니다.
 *
 *    ➡️ Spring Data JPA의 JpaRepository를 상속받아 기본적인 데이터베이스 작업을 자동으로 제공합니다.
 *    ➡️ 이 인터페이스는 Product 엔티티를 데이터베이스에 저장하거나 조회하는 데 사용됩니다.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * ✅ 특정 사용자가 등록한 모든 상품을 페이징하여 조회합니다.
     *
     *    ➡️ 주어진 사용자(User)에 해당하는 상품 목록을 페이지네이션(Pageable) 방식으로 반환합니다.
     *
     * @param user 조회하려는 상품의 소유자(User)입니다.
     * @param pageable 페이징 정보를 담고 있는 Pageable 객체입니다.
     * @return Page<Product> 주어진 사용자에 해당하는 상품 목록이 페이지네이션된 형태로 반환됩니다.
     */
    Page<Product> findAllByUser(User user, Pageable pageable);

    /**
     * ✅ 특정 사용자가 소유한 상품 중에서 지정된 폴더에 등록된 상품 목록을 페이징하여 조회합니다.
     *
     *    ➡️ 주어진 사용자(User)가 소유한 상품 중에서, 폴더 ID가 `folderId`인 폴더에 등록된 상품들을 페이지네이션(Pageable) 방식으로 반환합니다.
     *    ➡️ 폴더 ID와 사용자가 소유한 상품을 기반으로 필터링하여 조회하며, 페이지 정보를 통해 결과를 반환합니다.
     *
     * @param user 조회하려는 상품의 소유자(User)입니다. 이 사용자가 소유한 상품들 중에서 폴더에 등록된 상품을 필터링합니다.
     * @param folderId 조회할 폴더의 고유 ID입니다. 이 폴더에 등록된 상품들을 필터링합니다.
     * @param pageable 페이징 정보를 담고 있는 Pageable 객체입니다. 페이지 번호, 페이지 크기, 정렬 기준 등을 포함합니다.
     * @return Page<Product> 주어진 사용자와 폴더 ID에 해당하는 상품 목록이 페이지네이션된 형태로 반환됩니다.
     *         페이지네이션된 상품 목록은 `Product` 엔티티 객체를 포함합니다.
     */
    Page<Product> findAllByUserAndProductFolderList_FolderId(User user, Long folderId, Pageable pageable);
}