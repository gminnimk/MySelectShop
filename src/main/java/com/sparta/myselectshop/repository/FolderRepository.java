package com.sparta.myselectshop.repository;

import com.sparta.myselectshop.entity.Folder;
import com.sparta.myselectshop.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ✅ FolderRepository는 `Folder` 엔티티에 대한 데이터베이스 작업을 처리하는 JPA 리포지토리입니다.
 *
 *    ➡️ Spring Data JPA의 `JpaRepository`를 확장하여 기본적인 CRUD (Create, Read, Update, Delete) 작업을 지원합니다.
 *    ➡️ `Folder` 엔티티와 `Long` 타입의 기본 키를 사용하는 리포지토리입니다.
 */
public interface FolderRepository extends JpaRepository<Folder, Long> {

    /**
     * ✅ 주어진 사용자와 관련된 모든 폴더를 조회합니다.
     *
     * @param user 폴더를 조회할 사용자 객체입니다.
     * @return 사용자가 소유한 폴더의 리스트입니다.
     */
    List<Folder> findAllByUser(User user);

    /**
     * ✅ 주어진 사용자와 폴더 이름 목록을 기준으로 폴더를 조회합니다.
     *
     * @param user 사용자를 기준으로 폴더를 조회합니다.
     * @param folderNames 조회할 폴더 이름의 리스트입니다.
     * @return 사용자가 소유한 폴더 중에서 주어진 이름 목록과 일치하는 폴더의 리스트입니다.
     */
    List<Folder> findAllByUserAndNameIn(User user, List<String> folderNames);
}
