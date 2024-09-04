package com.sparta.myselectshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.myselectshop.dto.KakaoUserInfoDto;
import com.sparta.myselectshop.jwt.JwtUtil;
import com.sparta.myselectshop.repository.UserRepository;
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
 *    ➡️ 이 클래스는 카카오 OAuth 2.0 인증 과정을 통해 사용자의 카카오 계정 정보를 가져옵니다.
 *    ➡️ 인가 코드로 액세스 토큰을 요청하고, 그 토큰을 사용해 카카오 사용자 정보를 받아옵니다.
 *    ➡️ 받아온 사용자 정보를 기반으로 사용자 계정을 생성하거나 로그인 처리 등을 할 수 있습니다.
 *    ➡️ JWT 토큰을 생성하여 클라이언트에게 반환할 수 있습니다.
 */
@Slf4j(topic = "KAKAO Login") // 로그를 남기기 위한 어노테이션으로, 로그 주제를 "KAKAO Login"으로 설정합니다.
@Service // 이 클래스가 스프링의 서비스 컴포넌트임을 나타냅니다.
@RequiredArgsConstructor // Lombok 어노테이션으로, final 필드에 대한 생성자를 자동으로 생성합니다.
public class KakaoService {

    // 의존성 주입을 위한 final 필드 선언
    private final PasswordEncoder passwordEncoder; // 비밀번호 인코딩을 위한 PasswordEncoder
    private final UserRepository userRepository; // 사용자 정보를 처리하는 리포지토리
    private final RestTemplate restTemplate; // 외부 API 호출을 위한 RestTemplate
    private final JwtUtil jwtUtil; // JWT 토큰 생성을 위한 JwtUtil

    /**
     * ✅ 카카오 로그인 프로세스를 수행합니다.
     *
     *    ➡️ 인가 코드를 받아 카카오로부터 액세스 토큰을 얻고, 해당 토큰을 이용해 카카오 사용자 정보를 가져옵니다.
     *
     * @param code 카카오 서버로부터 받은 인가 코드입니다.
     * @return String 사용자 정보를 처리한 후 반환되는 결과 값 (현재는 null 반환).
     * @throws JsonProcessingException JSON 파싱 중 발생할 수 있는 예외입니다.
     */
    public String kakaoLogin(String code) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getToken(code);

        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        return null;
    }

    /**
     * ✅ 인가 코드를 사용하여 카카오 서버로부터 액세스 토큰을 요청합니다.
     *
     *    ➡️ 인가 코드를 카카오 토큰 API에 전송하여, 액세스 토큰을 얻습니다.
     *
     * @param code 카카오 서버로부터 받은 인가 코드입니다.
     * @return String 카카오로부터 받은 액세스 토큰입니다.
     * @throws JsonProcessingException JSON 파싱 중 발생할 수 있는 예외입니다.
     */
    private String getToken(String code) throws JsonProcessingException {
        log.info("인가코드 : " + code);

        // 요청 URL 생성
        URI uri = UriComponentsBuilder
            .fromUriString("https://kauth.kakao.com") // 카카오 인증 서버 URL
            .path("/oauth/token") // 토큰 요청 경로
            .encode()
            .build()
            .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "bc456203be456e6200be8293410a3751"); // 본인의 RESTAPI 키를 사용
        body.add("redirect_uri", "http://localhost:8080/api/user/kakao/callback"); // 인가 코드가 리다이렉트된 URI
        body.add("code", code); // 인가 코드 추가

        // 토큰 요청을 위한 HTTP 요청 생성
        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
            .post(uri)
            .headers(headers)
            .body(body);

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
            requestEntity,
            String.class
        );

        // HTTP 응답(JSON) -> 액세스 토큰 파싱
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    /**
     * ✅ 액세스 토큰을 사용하여 카카오 API로부터 사용자 정보를 가져옵니다.
     *
     *    ➡️ 액세스 토큰을 이용해 카카오 사용자 정보를 가져와서 KakaoUserInfoDto 객체로 변환합니다.
     *
     * @param accessToken 카카오 서버로부터 받은 액세스 토큰입니다.
     * @return KakaoUserInfoDto 카카오 사용자 정보가 담긴 DTO 객체입니다.
     * @throws JsonProcessingException JSON 파싱 중 발생할 수 있는 예외입니다.
     */
    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        log.info("accessToken : " + accessToken);

        // 요청 URL 생성
        URI uri = UriComponentsBuilder
            .fromUriString("https://kapi.kakao.com") // 카카오 API 서버 URL
            .path("/v2/user/me") // 사용자 정보 요청 경로
            .encode()
            .build()
            .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken); // 액세스 토큰을 Authorization 헤더에 추가
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 사용자 정보 요청을 위한 HTTP 요청 생성
        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
            .post(uri)
            .headers(headers)
            .body(new LinkedMultiValueMap<>()); // 빈 바디로 전송

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
            requestEntity,
            String.class
        );

        // JSON 응답을 파싱하여 사용자 정보 추출
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong(); // 카카오 사용자 ID
        String nickname = jsonNode.get("properties")
            .get("nickname").asText(); // 사용자 닉네임
        String email = jsonNode.get("kakao_account")
            .get("email").asText(); // 사용자 이메일

        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);

        // 사용자 정보 DTO 반환
        return new KakaoUserInfoDto(id, nickname, email);
    }
}