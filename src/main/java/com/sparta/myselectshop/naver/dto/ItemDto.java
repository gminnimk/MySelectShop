package com.sparta.myselectshop.naver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

/**
 * ✅ ItemDto 클래스는 네이버 API에서 반환된 아이템 정보를 담기 위한 데이터 전송 객체입니다.
 *
 *    ➡️ 네이버 쇼핑 API의 응답 데이터를 Java 객체로 변환하여 사용할 수 있도록 정의합니다.
 */

@Getter
@NoArgsConstructor
public class ItemDto {
    private String title; // 아이템의 제목
    private String link;  // 아이템의 링크
    private String image; // 아이템의 이미지 URL
    private int lprice;   // 아이템의 최저가

    /**
     * ✅ JSONObject를 기반으로 ItemDto 객체를 초기화합니다.
     *
     *    ➡️ 네이버 API로부터 받은 JSON 객체를 파싱하여 ItemDto의 필드에 값을 설정합니다.
     *
     * @param itemJson 네이버 API로부터 받은 아이템 정보를 담고 있는 JSON 객체입니다.
     */
    public ItemDto(JSONObject itemJson) {
        this.title = itemJson.getString("title");   // JSON 객체에서 제목을 가져와 설정
        this.link = itemJson.getString("link");     // JSON 객체에서 링크를 가져와 설정
        this.image = itemJson.getString("image");   // JSON 객체에서 이미지 URL을 가져와 설정
        this.lprice = itemJson.getInt("lprice");     // JSON 객체에서 최저가를 가져와 설정
    }
}