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

/*
NaverApiService라는 이름의 서비스 클래스로,
네이버의 검색 API를 사용하여 쇼핑 아이템을 검색하는 기능을 제공합니다.


- NaverApiService 클래스는 네이버 API와 통신하여 검색 요청을 보내고, 응답을 받아서 ItemDto 리스트로 변환하는 역할을 합니다.

- 이 클래스는 RESTful 웹 서비스와의 통신을 쉽게 처리할 수 있도록 RestTemplate과 UriComponentsBuilder를 사용합니다.

- JSON 응답을 파싱하여 필요한 데이터를 추출하고, 이를 ItemDto 객체로 변환하여 반환합니다.

- 로깅을 통해 디버깅과 모니터링이 용이하게 합니다.
 */

@Slf4j(topic = "NAVER API") //  Lombok을 사용하여 로깅 기능을 추가합니다. 로그 메시지를 남길 때 NAVER API라는 토픽을 사용합니다.
@Service // 이 클래스가 서비스 역할을 한다는 것을 Spring에게 알려줍니다.
public class NaverApiService {

    private final RestTemplate restTemplate;
    // 객체를 생성자로 초기화합니다. RestTemplate는 Spring에서 제공하는 클래스로, RESTful 웹 서비스와 상호 작용할 수 있도록 도와줍니다.

    public NaverApiService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public List<ItemDto> searchItems(String query) {
        // 요청 URL 만들기

        //UriComponentsBuilder를 사용하여 네이버 API의 요청 URL을 만듭니다.
        // display 파라미터로 검색 결과의 개수를 지정하고, query 파라미터로 검색어를 지정합니다.
        //log.info("uri = " + uri): 요청 URI를 로그에 남깁니다.
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/shop.json")
                .queryParam("display", 15)
                .queryParam("query", query)
                .encode()
                .build()
                .toUri();
        log.info("uri = " + uri);


        //RequestEntity를 사용하여 GET 요청을 생성하고, 헤더에 네이버 API 인증 정보를 추가합니다.
        //restTemplate.exchange(requestEntity, String.class): 네이버 API에 요청을 보내고 응답을 받습니다.
        //응답 상태 코드를 로그에 남깁니다.
        //응답 본문을 fromJSONtoItems 메서드를 통해 ItemDto 리스트로 변환합니다.
        RequestEntity<Void> requestEntity = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id", "19fQjtneKL8KX338zFEu")
                .header("X-Naver-Client-Secret", "reTmozLi4H")
                .build();

        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        log.info("NAVER API Status Code : " + responseEntity.getStatusCode());

        return fromJSONtoItems(responseEntity.getBody());
    }


    //JSON 응답을 파싱하여 ItemDto 객체의 리스트로 변환합니다.
    //responseEntity 문자열을 JSON 객체로 변환하고, items 배열을 추출합니다.
    //각 item을 ItemDto 객체로 변환하여 리스트에 추가합니다.
    public List<ItemDto> fromJSONtoItems(String responseEntity) {
        JSONObject jsonObject = new JSONObject(responseEntity);
        JSONArray items  = jsonObject.getJSONArray("items");
        List<ItemDto> itemDtoList = new ArrayList<>();

        for (Object item : items) {
            ItemDto itemDto = new ItemDto((JSONObject) item);
            itemDtoList.add(itemDto);
        }

        return itemDtoList;
    }
}