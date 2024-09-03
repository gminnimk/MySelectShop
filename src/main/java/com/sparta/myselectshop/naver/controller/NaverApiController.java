package com.sparta.myselectshop.naver.controller;

import com.sparta.myselectshop.naver.dto.ItemDto;
import com.sparta.myselectshop.naver.service.NaverApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ✅ NaverApiController 클래스는 네이버 API와의 상호작용을 처리하는 REST 컨트롤러입니다.
 *
 *    ➡️ 클라이언트의 요청을 받아 네이버 API 서비스에서 아이템을 검색하고 결과를 반환합니다.
 */
@RestController // 이 클래스가 RESTful 웹 서비스의 컨트롤러임을 나타냅니다.
@RequiredArgsConstructor // Lombok 어노테이션으로, final 필드에 대한 생성자를 자동 생성합니다.
@RequestMapping("/api") // 기본 URL 경로를 "/api"로 설정합니다.
public class NaverApiController {

    private final NaverApiService naverApiService; // 네이버 API와 상호작용할 서비스 클래스의 의존성 주입

    /**
     * ✅ 클라이언트의 검색 쿼리를 받아 네이버 API를 통해 아이템을 검색합니다.
     *
     *    ➡️ 네이버 API 서비스의 `searchItems` 메서드를 호출하여 검색 결과를 `ItemDto` 리스트로 반환합니다.
     *
     * @param query 검색할 아이템의 쿼리 문자열입니다.
     * @return List<ItemDto> 검색된 아이템 정보를 담고 있는 `ItemDto` 객체의 리스트입니다.
     */
    @GetMapping("/search") // HTTP GET 요청을 처리하며, 경로는 "/api/search"입니다.
    public List<ItemDto> searchItems(@RequestParam String query)  {
        // 네이버 API 서비스를 통해 아이템 검색 결과를 가져옵니다.
        return naverApiService.searchItems(query);
    }
}