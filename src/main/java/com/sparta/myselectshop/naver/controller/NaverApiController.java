package com.sparta.myselectshop.naver.controller;


import com.sparta.myselectshop.naver.dto.ItemDto;
import com.sparta.myselectshop.naver.service.NaverApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/*
Spring Framework를 사용하여 작성된 NaverApiController 클래스입니다.
이 클래스는 네이버 API를 이용한 검색 기능을 제공하는 RESTful 웹 서비스의 컨트롤러 역할을 합니다
 */

@RestController // 이 클래스가 RESTful 컨트롤러임을 나타내며, 모든 메서드에 자동으로 @ResponseBody가 적용되어 JSON 형식으로 응답합니다.
@RequiredArgsConstructor // naverApiService 필드를 초기화하는 생성자를 Lombok이 자동으로 생성해줍니다.
@RequestMapping("/api") //  이 클래스의 모든 메서드는 기본 URL /api를 가집니다.


// 네이버 API 검색 기능을 제공하는 RESTful 웹 서비스의 컨트롤러

// 사용자는 /api/search 엔드포인트에 query 파라미터를 포함하여 GET 요청을 보내면,
// NaverApiService를 통해 검색이 수행되고, 결과가 ItemDto 리스트로 반환됩니다.
// 서비스 클래스를 이용하여 비즈니스 로직을 분리하고, 컨트롤러 클래스는 요청을 처리하는 역할에 집중하도록 합니다
public class NaverApiController {

    private final NaverApiService naverApiService;

    @GetMapping("/search") // HTTP GET 요청을 처리하며, URL 경로는 /api/search가 됩니다.
    public List<ItemDto> searchItems(@RequestParam String query)  { //  query 파라미터를 받아서 네이버 API를 통해 검색을 수행하고, 결과를 ItemDto 객체의 리스트로 반환합니다.
        return naverApiService.searchItems(query);
        // URL 파라미터 query를 받아옵니다.
        // 예를 들어, /api/search?query=apple과 같이 요청할 때 query 값으로 "apple"이 전달됩니다.
    }
}