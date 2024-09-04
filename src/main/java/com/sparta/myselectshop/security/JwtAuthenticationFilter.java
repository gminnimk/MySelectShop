package com.sparta.myselectshop.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.myselectshop.dto.LoginRequestDto;
import com.sparta.myselectshop.entity.UserRoleEnum;
import com.sparta.myselectshop.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

/**
 * ✅ JwtAuthenticationFilter 클래스는 사용자의 로그인 요청을 처리하고 JWT를 생성하는 필터 클래스입니다.
 *
 *    ➡️ 사용자의 인증 정보(아이디, 비밀번호)를 검증하고, 인증에 성공하면 JWT 토큰을 생성하여 클라이언트에게 전달하는 역할을 합니다.
 */
@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    /**
     * ✅ JwtAuthenticationFilter 생성자
     *
     *    ➡️ JwtUtil 객체를 받아와 필터의 초기 URL을 "/api/user/login"으로 설정합니다.
     *
     * @param jwtUtil JWT 유틸리티 클래스
     */
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/user/login");
    }

    /**
     * ✅ 사용자의 인증 요청을 처리합니다.
     *
     *    ➡️ 요청 본문에서 사용자 정보를 추출하여 인증을 시도하고, 결과를 반환합니다.
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @return Authentication 인증 객체
     * @throws AuthenticationException 인증 실패 시 예외를 던집니다.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // 요청 본문에서 로그인 정보를 DTO로 변환
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            // UsernamePasswordAuthenticationToken 생성 후 인증 시도
            return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                    requestDto.getUsername(),
                    requestDto.getPassword(),
                    null
                )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * ✅ 인증에 성공했을 때 호출됩니다.
     *
     *    ➡️ 인증에 성공한 사용자의 정보를 기반으로 JWT 토큰을 생성하여 응답 헤더에 추가합니다.
     *
     * @param request      HTTP 요청 객체
     * @param response     HTTP 응답 객체
     * @param chain        필터 체인
     * @param authResult   인증 결과 객체
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        // JWT 토큰 생성 및 응답 헤더에 추가
        String token = jwtUtil.createToken(username, role);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
    }

    /**
     * ✅ 인증에 실패했을 때 호출됩니다.
     *
     *    ➡️ 인증 실패 시 응답 상태를 401로 설정합니다.
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param failed   인증 실패 예외
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401); // 인증 실패 시 401 상태 코드 반환
    }
}