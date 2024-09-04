package com.sparta.myselectshop.security;

import com.sparta.myselectshop.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * ✅ JwtAuthorizationFilter 클래스는 JWT를 이용한 인가 작업을 수행하는 필터 클래스입니다.
 *
 * ➡️ Spring Security의 필터 체인에서 JWT를 검증하고, 해당 JWT로부터 사용자 정보를 추출하여 인증을 처리합니다.
 */
@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * ✅ JwtAuthorizationFilter 생성자
     *
     * ➡️ JWT 유틸리티 클래스와 사용자 정보를 로드하는 서비스 클래스의 의존성을 주입받습니다.
     *
     * @param jwtUtil            JWT 유틸리티 클래스
     * @param userDetailsService 사용자 정보를 로드하는 서비스 클래스
     */
    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * ✅ HTTP 요청을 필터링하여 JWT를 검증하고, 인증을 처리합니다.
     *
     * ➡️ JWT가 유효한 경우, 사용자 정보를 추출하고 Spring Security 컨텍스트에 인증 객체를 설정합니다.
     *
     * @param req         HTTP 요청 객체
     * @param res         HTTP 응답 객체
     * @param filterChain 필터 체인 객체
     * @throws ServletException 서블릿 예외
     * @throws IOException      입출력 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
        FilterChain filterChain) throws ServletException, IOException {

        // 요청 헤더에서 JWT 토큰을 가져옵니다.
        String tokenValue = jwtUtil.getJwtFromHeader(req);

        // 토큰 값이 존재하는지 확인합니다.
        if (StringUtils.hasText(tokenValue)) {

            // JWT 토큰의 유효성을 검증합니다.
            if (!jwtUtil.validateToken(tokenValue)) {
                log.error("Token Error");
                return;
            }

            // 토큰에서 사용자 정보를 추출합니다.
            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            try {
                // 인증을 설정합니다.
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        // 다음 필터를 호출합니다.
        filterChain.doFilter(req, res);
    }

    /**
     * ✅ 인증 처리를 수행합니다.
     *
     * ➡️ 주어진 사용자 이름을 기반으로 인증 객체를 생성하여, SecurityContext에 설정합니다.
     *
     * @param username 인증할 사용자 이름
     */
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        // SecurityContextHolder에 인증 컨텍스트를 설정합니다.
        SecurityContextHolder.setContext(context);
    }

    /**
     * ✅ 인증 객체를 생성합니다.
     *
     * ➡️ 주어진 사용자 이름을 기반으로 UserDetails 객체를 로드하고, 이를 사용해 인증 객체를 생성합니다.
     *
     * @param username 인증할 사용자 이름
     * @return Authentication 생성된 인증 객체
     */
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities());
    }
}