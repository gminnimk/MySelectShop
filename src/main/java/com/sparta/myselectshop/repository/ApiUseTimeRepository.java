package com.sparta.myselectshop.repository;

import com.sparta.myselectshop.entity.ApiUseTime;
import com.sparta.myselectshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * ✅ ApiUseTimeRepository는 API 사용 시간 데이터를 관리하는 데이터 액세스 레이어입니다.
 *
 *    ➡️ JpaRepository를 상속받아, API 사용 시간 엔티티에 대한 기본적인 CRUD (Create, Read, Update, Delete) 작업을 제공합니다.
 */
public interface ApiUseTimeRepository extends JpaRepository<ApiUseTime, Long> {

    /**
     * ✅ 특정 사용자에 대한 API 사용 시간을 조회합니다.
     *
     *    ➡️ 사용자와 관련된 API 사용 시간 데이터를 검색할 때 사용됩니다.
     *
     * @param user 조회할 사용자 엔티티
     * @return Optional<ApiUseTime> 해당 사용자의 API 사용 시간 데이터를 담고 있는 Optional 객체입니다.
     */
    Optional<ApiUseTime> findByUser(User user);
}