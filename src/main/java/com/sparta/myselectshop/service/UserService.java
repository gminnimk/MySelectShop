package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.SignupRequestDto;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.entity.UserRoleEnum;
import com.sparta.myselectshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * ✅ UserService 클래스는 사용자 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 *
 *    ➡️ 사용자 회원 가입과 관련된 기능을 제공하며, 사용자 정보를 관리합니다.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN: 관리자 등록 시 사용되는 인증 토큰
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    /**
     * ✅ 사용자 회원 가입을 처리하는 메서드입니다.
     *
     *    ➡️ 사용자의 이름, 비밀번호, 이메일을 바탕으로 회원 가입을 진행하며,
     *      중복된 사용자 이름이나 이메일을 확인하고, 관리자 권한을 부여합니다.
     *
     * @param requestDto 회원 가입에 필요한 사용자 요청 데이터를 포함하는 DTO입니다.
     * @throws IllegalArgumentException 사용자가 이미 존재하거나 이메일이 중복된 경우,
     *                                  또는 관리자 암호가 틀린 경우에 발생하는 예외입니다.
     */
    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 이메일 중복 확인
        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = new User(username, password, email, role);
        userRepository.save(user);
    }
}
