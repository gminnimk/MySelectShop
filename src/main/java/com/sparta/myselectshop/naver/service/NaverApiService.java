package com.sparta.myselectshop.naver.service;

import com.sparta.myselectshop.naver.dto.ItemDto;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * ✅ NaverApiService 클래스는 네이버 API와의 통신을 처리하는 서비스 클래스입니다.
 *
 *    ➡️ 네이버 쇼핑 API를 호출하여 아이템 정보를 검색하고, 응답 데이터를 가공하는 역할을 합니다.
 */
@Slf4j(topic = "NAVER API") // Lombok 어노테이션으로, Naver API와 관련된 로그를 출력합니다.
@Service // Spring의 서비스 컴포넌트로 등록되어 스프링 컨텍스트에서 관리됩니다.
public class NaverApiService {

    private final RestTemplate restTemplate; // REST API 호출을 위한 RestTemplate

    /**
     * ✅ NaverApiService의 생성자입니다.
     *
     *    ➡️ RestTemplate을 초기화하여 REST API 호출에 사용됩니다.
     *
     * @param builder RestTemplateBuilder를 통해 RestTemplate을 생성합니다.
     */
    public NaverApiService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    /**
     * ✅ 네이버 쇼핑 API에 검색 요청을 보내고, 아이템 리스트를 반환합니다.
     *
     *    ➡️ 쿼리 파라미터를 사용하여 네이버 쇼핑 API를 호출하고, 결과를 ItemDto 리스트로 변환하여 반환합니다.
     *
     * @param query 검색 쿼리 파라미터입니다.
     * @return List<ItemDto> 네이버 API로부터 받은 아이템 정보의 리스트입니다.
     */
    public List<ItemDto> searchItems(String query) {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
            .fromUriString("https://openapi.naver.com")
            .path("/v1/search/shop.json")
            .queryParam("display", 15) // 최대 15개의 결과를 요청
            .queryParam("query", query) // 검색 쿼리
            .encode()
            .build()
            .toUri();
        log.info("uri = " + uri); // 생성된 URI 로그 출력

        // 요청 엔티티 생성: GET 메서드와 헤더에 클라이언트 ID 및 비밀번호 추가
        RequestEntity<Void> requestEntity = RequestEntity
            .get(uri)
            .header("X-Naver-Client-Id", "{Client-Id}") // 클라이언트 ID
            .header("X-Naver-Client-Secret", "{Client-Secret}") // 클라이언트 비밀번호
            .build();

        // 네이버 API 호출
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        log.info("NAVER API Status Code : " + responseEntity.getStatusCode()); // 응답 상태 코드 로그 출력

        // JSON 응답 데이터를 ItemDto 리스트로 변환하여 반환
        return fromJSONtoItems(responseEntity.getBody());
    }

    /**
     * ✅ JSON 응답 데이터를 ItemDto 객체 리스트로 변환합니다.
     *
     *    ➡️ 네이버 API로부터 받은 JSON 응답을 파싱하여 ItemDto 리스트를 생성합니다.
     *
     * @param responseEntity JSON 형식의 응답 데이터입니다.
     * @return List<ItemDto> JSON 응답 데이터를 기반으로 생성된 ItemDto 객체 리스트입니다.
     */
    public List<ItemDto> fromJSONtoItems(String responseEntity) {
        JSONObject jsonObject = new JSONObject(responseEntity); // JSON 객체로 변환
        JSONArray items = jsonObject.getJSONArray("items"); // "items" 배열 추출
        List<ItemDto> itemDtoList = new ArrayList<>(); // ItemDto 리스트 생성

        // 각 아이템을 ItemDto로 변환하여 리스트에 추가
        for (Object item : items) {
            ItemDto itemDto = new ItemDto((JSONObject) item);
            itemDtoList.add(itemDto);
        }

        return itemDtoList; // 변환된 리스트 반환
    }
}
