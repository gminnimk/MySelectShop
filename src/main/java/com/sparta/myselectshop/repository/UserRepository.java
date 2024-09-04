package com.sparta.myselectshop.repository;

import com.sparta.myselectshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * ✅ UserRepository 인터페이스는 사용자 엔티티에 대한 데이터 접근을 담당하는 JPA 리포지토리입니다.
 *
 *    ➡️ 사용자 엔티티의 CRUD(Create, Read, Update, Delete) 작업을 지원하며,
 *      사용자 이름과 이메일로 사용자 정보를 조회하는 메서드를 제공합니다.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * ✅ 사용자 이름으로 사용자 정보를 조회합니다.
     *
     *    ➡️ 주어진 사용자 이름을 기반으로 사용자 정보를 조회하고,
     *      해당 사용자가 존재하는 경우 Optional에 감싸서 반환합니다.
     *
     * @param username 사용자 이름
     * @return Optional<User> 사용자 이름에 해당하는 사용자 정보가 포함된 Optional 객체
     */
    Optional<User> findByUsername(String username);

    /**
     * ✅ 이메일로 사용자 정보를 조회합니다.
     *
     *    ➡️ 주어진 이메일을 기반으로 사용자 정보를 조회하고,
     *      해당 이메일에 연결된 사용자가 존재하는 경우 Optional에 감싸서 반환합니다.
     *
     * @param email 사용자 이메일
     * @return Optional<User> 사용자 이메일에 해당하는 사용자 정보가 포함된 Optional 객체
     */
    Optional<User> findByEmail(String email);
}