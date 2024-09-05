package com.sparta.myselectshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.myselectshop.dto.KakaoUserInfoDto;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.entity.UserRoleEnum;
import com.sparta.myselectshop.jwt.JwtUtil;
import com.sparta.myselectshop.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * ✅ KakaoService 클래스는 카카오 로그인을 처리하는 서비스 클래스입니다.
 *
 *    ➡️ 카카오 OAuth 2.0 인증 과정을 통해 카카오 사용자 정보를 가져오고, 그 정보를 바탕으로
 *       회원가입 또는 로그인 처리와 JWT 토큰 생성을 담당합니다.
 */
@Slf4j(topic = "KAKAO Login") // 로그 주제를 "KAKAO Login"으로 설정
@Service // Spring의 서비스 컴포넌트임을 나타내는 어노테이션
@RequiredArgsConstructor // final 필드를 매개변수로 받는 생성자를 자동으로 생성하는 Lombok 어노테이션
public class KakaoService {

    // 필드 주입: KakaoService 클래스가 필요한 의존성을 자동 주입받음
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화를 위한 PasswordEncoder
    private final UserRepository userRepository; // 사용자 정보를 처리하는 Repository
    private final RestTemplate restTemplate; // 외부 API 호출을 위한 RestTemplate
    private final JwtUtil jwtUtil; // JWT 토큰 생성 유틸리티 클래스

    /**
     * ✅ 카카오 로그인 프로세스를 수행하는 메서드입니다.
     *
     *    ➡️ 인가 코드를 받아서 카카오 API를 통해 액세스 토큰을 얻고, 해당 토큰으로 사용자 정보를 가져온 후,
     *        회원가입 또는 로그인 처리를 수행하고 JWT 토큰을 생성하여 반환합니다.
     *
     * @param code 카카오 서버로부터 받은 인가 코드
     * @return JWT 토큰을 생성하여 반환
     * @throws JsonProcessingException JSON 파싱 중 발생할 수 있는 예외
     */
    public String kakaoLogin(String code) throws JsonProcessingException {
        // 1. 인가 코드로 액세스 토큰 요청
        String accessToken = getToken(code);

        // 2. 액세스 토큰으로 카카오 사용자 정보 가져오기
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 3. 카카오 사용자 정보로 회원가입 또는 기존 회원 찾기
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        // 4. JWT 토큰 생성
        String createToken = jwtUtil.createToken(kakaoUser.getUsername(), kakaoUser.getRole());

        return createToken;
    }

    /**
     * ✅ 인가 코드를 사용하여 카카오 서버로부터 액세스 토큰을 요청하는 메서드입니다.
     *
     *    ➡️ 인가 코드를 카카오 서버의 토큰 API에 전송하여, 액세스 토큰을 받아옵니다.
     *
     * @param code 카카오 서버로부터 받은 인가 코드
     * @return 카카오 서버로부터 받은 액세스 토큰
     * @throws JsonProcessingException JSON 파싱 중 발생할 수 있는 예외
     */
    private String getToken(String code) throws JsonProcessingException {
        log.info("인가코드 : " + code); // 인가 코드를 로그에 기록

        // 카카오 토큰 요청 URL 생성
        URI uri = UriComponentsBuilder
            .fromUriString("https://kauth.kakao.com") // 카카오 인증 서버 주소
            .path("/oauth/token") // 토큰 요청 경로
            .encode()
            .build()
            .toUri();

        // HTTP 요청 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 바디 생성 (필수 파라미터 포함)
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code"); // OAuth 2.0 권한 부여 방식
        body.add("client_id", "bc456203be456e6200be8293410a3751"); // 클라이언트 ID
        body.add("redirect_uri", "http://localhost:8080/api/user/kakao/callback"); // 인가 코드가 리다이렉트된 URI
        body.add("code", code); // 전달받은 인가 코드

        // HTTP 요청 생성
        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
            .post(uri) // POST 요청
            .headers(headers) // 헤더 추가
            .body(body); // 바디 추가

        // HTTP 요청 보내기 및 응답 수신
        ResponseEntity<String> response = restTemplate.exchange(
            requestEntity,
            String.class
        );

        // JSON 응답에서 액세스 토큰 추출
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText(); // 액세스 토큰 반환
    }

    /**
     * ✅ 액세스 토큰을 사용하여 카카오 API로부터 사용자 정보를 가져오는 메서드입니다.
     *
     *    ➡️ 액세스 토큰으로 카카오 사용자 정보를 가져와서 KakaoUserInfoDto 객체로 변환하여 반환합니다.
     *
     * @param accessToken 카카오 서버로부터 받은 액세스 토큰
     * @return KakaoUserInfoDto 카카오 사용자 정보가 담긴 DTO 객체
     * @throws JsonProcessingException JSON 파싱 중 발생할 수 있는 예외
     */
    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        log.info("accessToken : " + accessToken); // 액세스 토큰을 로그에 기록

        // 카카오 사용자 정보 요청 URL 생성
        URI uri = UriComponentsBuilder
            .fromUriString("https://kapi.kakao.com") // 카카오 API 서버 주소
            .path("/v2/user/me") // 사용자 정보 요청 경로
            .encode()
            .build()
            .toUri();

        // HTTP 요청 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken); // 액세스 토큰을 Authorization 헤더에 추가
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 사용자 정보 요청을 위한 HTTP 요청 생성
        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
            .post(uri)
            .headers(headers) // 헤더 설정
            .body(new LinkedMultiValueMap<>()); // 빈 바디

        // HTTP 요청 보내기 및 응답 수신
        ResponseEntity<String> response = restTemplate.exchange(
            requestEntity,
            String.class
        );

        // JSON 응답에서 사용자 정보 추출
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong(); // 카카오 사용자 ID
        String nickname = jsonNode.get("properties")
            .get("nickname").asText(); // 사용자 닉네임
        String email = jsonNode.get("kakao_account")
            .get("email").asText(); // 사용자 이메일

        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email); // 로그 기록

        // 카카오 사용자 정보를 담은 DTO 반환
        return new KakaoUserInfoDto(id, nickname, email);
    }

    /**
     * ✅ 카카오 사용자 정보로 새로운 회원을 등록하거나 기존 회원을 반환하는 메서드입니다.
     *
     *    ➡️ DB에 카카오 ID로 조회한 사용자가 없으면, 새로운 사용자를 등록합니다.
     *    ➡️ 카카오 이메일과 동일한 이메일을 가진 사용자가 있으면 해당 사용자에 카카오 ID를 업데이트합니다.
     *
     * @param kakaoUserInfo 카카오 사용자 정보 DTO
     * @return User 등록된 사용자 또는 기존 사용자
     */
    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        // 카카오 ID로 기존 회원 조회
        Long kakaoId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);

        // 만약 기존 회원이 없다면 새로운 회원 등록
        if (kakaoUser == null) {
            // 동일한 이메일을 가진 사용자가 있는지 확인
            String kakaoEmail = kakaoUserInfo.getEmail();
            User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);

            if (sameEmailUser != null) {
                // 동일 이메일 사용자가 있으면 해당 사용자에 카카오 ID 추가
                kakaoUser = sameEmailUser.kakaoIdUpdate(kakaoId);
            } else {
                // 신규 회원가입 처리
                String password = UUID.randomUUID().toString(); // 랜덤 UUID 생성
                String encodedPassword = passwordEncoder.encode(password); // 비밀번호 암호화

                kakaoUser = new User(
                    kakaoUserInfo.getNickname(), // 사용자 닉네임
                    encodedPassword, // 암호화된 비밀번호
                    kakaoEmail, // 이메일
                    UserRoleEnum.USER, // 기본 사용자 권한
                    kakaoId // 카카오 ID
                );
            }

            // 사용자 정보 저장
            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }
}
