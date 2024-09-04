package com.sparta.myselectshop.config;

import com.sparta.myselectshop.jwt.JwtUtil;
import com.sparta.myselectshop.security.JwtAuthenticationFilter;
import com.sparta.myselectshop.security.JwtAuthorizationFilter;
import com.sparta.myselectshop.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * ✅ WebSecurityConfig 클래스는 Spring Security 설정을 담당하는 클래스입니다.
 *
 *   ➡️ JWT를 사용한 인증 및 권한 부여 설정, 비밀번호 인코딩, 필터 체인 설정 등을 정의합니다.
 */
@Configuration
@EnableWebSecurity // Spring Security를 활성화 시킵니다.
@RequiredArgsConstructor
public class WebSecurityConfig {

    // JWT 유틸리티 클래스, 사용자 정보 서비스, 인증 구성 객체를 주입받습니다.
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;

    /**
     * ✅ PasswordEncoder 빈을 생성합니다.
     *
     *   ➡️ 비밀번호를 암호화하기 위해 BCryptPasswordEncoder를 사용합니다.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * ✅ AuthenticationManager 빈을 생성합니다.
     *
     *   ➡️ Spring Security에서 인증을 처리하는 매니저 객체를 반환합니다.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * ✅ JwtAuthenticationFilter 빈을 생성합니다.
     *
     *   ➡️ JWT 인증 필터를 생성하며, 인증 매니저를 설정합니다.
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    /**
     * ✅ JwtAuthorizationFilter 빈을 생성합니다.
     *
     *   ➡️ JWT를 사용한 권한 부여 필터를 생성합니다.
     */
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    /**
     * ✅ SecurityFilterChain 빈을 구성합니다.
     *
     *   ➡️ HTTP 보안 설정을 정의합니다.
     *
     * @param http HTTP 보안 설정 객체
     * @return 설정된 SecurityFilterChain 객체를 반환합니다.
     * @throws Exception 예외가 발생할 수 있습니다.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 방어 기능 비활성화
        http.csrf((csrf) -> csrf.disable());

        // 세션을 사용하지 않고, JWT 방식을 사용하도록 설정
        http.sessionManagement((sessionManagement) ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // 요청에 대한 권한 설정
        http.authorizeHttpRequests((authorizeHttpRequests) ->
            authorizeHttpRequests
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 리소스(static resources) 접근을 허용
                .requestMatchers("/").permitAll() // 메인 페이지 접근을 허용
                .requestMatchers("/api/user/**").permitAll() // '/api/user/'로 시작하는 요청을 모두 허용
                .anyRequest().authenticated() // 그 외의 모든 요청은 인증 필요
        );

        // 로그인 페이지 설정
        http.formLogin((formLogin) ->
            formLogin
                .loginPage("/api/user/login-page").permitAll() // 로그인 페이지 접근을 허용
        );

        // 필터 체인에 커스텀 JWT 필터를 추가
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}