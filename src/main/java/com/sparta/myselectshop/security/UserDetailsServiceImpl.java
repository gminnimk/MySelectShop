package com.sparta.myselectshop.security;

import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * ✅ UserDetailsServiceImpl 클래스는 Spring Security에서 사용자 세부 정보를 제공하는 서비스 클래스입니다.
 *
 *    ➡️ 사용자 이름을 기반으로 사용자의 정보를 로드하고, 인증을 위한 UserDetails 객체를 반환합니다.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * ✅ 사용자 이름을 기반으로 사용자 정보를 로드합니다.
     *
     *    ➡️ 사용자 이름을 통해 데이터베이스에서 사용자 정보를 조회하고,
     *      조회된 정보를 UserDetails 객체로 반환합니다.
     *
     * @param username 사용자 이름
     * @return UserDetails 사용자의 세부 정보를 담고 있는 UserDetails 객체
     * @throws UsernameNotFoundException 사용자 이름에 해당하는 사용자가 데이터베이스에 존재하지 않을 경우 발생하는 예외
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자 이름을 통해 데이터베이스에서 사용자 정보를 조회합니다.
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));

        // 조회된 사용자 정보를 UserDetailsImpl 객체로 변환하여 반환합니다.
        return new UserDetailsImpl(user);
    }
}
