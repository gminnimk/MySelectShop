package com.sparta.myselectshop.jwt;

import com.sparta.myselectshop.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * ✅ JwtUtil 클래스는 JWT 토큰의 생성, 검증 및 파싱을 담당하는 유틸리티 클래스입니다.
 *
 *      ➡️ 사용자 인증과 권한 부여를 위해 JWT(Json Web Token)를 사용하여 토큰을 생성하고,
 *      ➡️ 클라이언트의 요청에서 토큰을 추출하고, 유효성을 검증하며, 토큰에서 사용자 정보를 파싱하는 기능을 제공합니다.
 */
@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    // Header에서 JWT 토큰을 가져올 때 사용하는 키 값
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // 사용자 권한을 저장하는 클레임의 키 값
    public static final String AUTHORIZATION_KEY = "auth";

    // JWT 토큰의 접두사
    public static final String BEARER_PREFIX = "Bearer ";

    // JWT 토큰의 만료 시간 (60분)
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}") // Base64로 인코딩된 비밀키
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    /**
     * ✅ secretKey를 Base64로 디코딩하여 HMAC SHA 알고리즘용 키 객체를 초기화합니다.
     *
     *      ➡️ `@PostConstruct` 어노테이션을 통해 이 메서드는 의존성 주입이 완료된 후 실행됩니다.
     */
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    /**
     * ✅ JWT 토큰을 생성합니다.
     *
     *      ➡️ 사용자 이름과 권한을 기반으로 JWT 토큰을 생성하고, 만료 시간을 설정합니다.
     *
     * @param username 사용자 이름(식별자)
     * @param role 사용자 권한(Enum)
     * @return 생성된 JWT 토큰
     */
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
            Jwts.builder()
                .setSubject(username) // 사용자 식별자값(ID)
                .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간 설정
                .setIssuedAt(date) // 토큰 발급일 설정
                .signWith(key, signatureAlgorithm) // HMAC SHA-256 알고리즘을 사용한 서명
                .compact();
    }

    /**
     * ✅ HTTP 요청 헤더에서 JWT 토큰을 가져옵니다.
     *
     *      ➡️ 요청 헤더에서 `Authorization` 값을 가져와 `Bearer ` 접두사 이후의 토큰 값을 반환합니다.
     *
     * @param request HTTP 요청 객체
     * @return JWT 토큰 값 (접두사 제거)
     */
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * ✅ JWT 토큰의 유효성을 검사합니다.
     *
     *      ➡️ 토큰이 유효한지 확인하고, 서명 및 구조가 올바른지 검사합니다.
     *
     * @param token 검사할 JWT 토큰
     * @return 토큰이 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않은 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰입니다.");
        }
        return false;
    }

    /**
     * ✅ JWT 토큰에서 사용자 정보를 추출합니다.
     *
     *      ➡️ JWT 토큰에서 클레임 정보를 추출하여 사용자 정보를 반환합니다.
     *
     * @param token JWT 토큰
     * @return 추출된 사용자 정보 (Claims 객체)
     */
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
