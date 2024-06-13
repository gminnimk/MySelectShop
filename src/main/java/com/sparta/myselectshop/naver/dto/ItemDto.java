package com.sparta.myselectshop.naver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

/*
 Java의 Lombok 라이브러리와 JSON을 사용하여 작성된 ItemDto 클래스입니다.
 이 클래스는 특정 아이템에 대한 정보를 담는 역할을 합니다.


 ItemDto 클래스는 JSON 객체로부터 데이터를 받아서 간편하게 Java 객체로 변환할 수 있게 해줍니다. Lombok을 사용하여 보일러플레이트 코드를 줄이고,
 JSON 라이브러리를 사용하여 JSON 데이터를 쉽게 처리할 수 있도록 합니다
 */

@Getter // Lombok 라이브러리가 자동으로 모든 필드에 대해 getter 메서드를 생성해줍니다.
        // 예를 들어, getTitle(), getLink(), getImage(), getLprice() 메서드가 자동으로 생성됩니다.
@NoArgsConstructor // Lombok이 파라미터가 없는 기본 생성자를 자동으로 생성해줍니다.

// 현재 ItemDto 클래스는 title, link, image, lprice 네 가지 필드를 가지고 있습니다.
public class ItemDto {
    private String title; // 아이템의 제목을 저장하는 문자열 필드입니다.
    private String link; // 아이템에 대한 링크를 저장하는 문자열 필드입니다.
    private String image; // 아이템 이미지 URL을 저장하는 문자열 필드입니다.
    private int lprice; // 아이템의 가격을 저장하는 정수형 필드입니다.

    public ItemDto(JSONObject itemJson) { // 생성자는 JSONObject를 매개변수로 받아서 ItemDto 객체를 초기화합니다.
        this.title = itemJson.getString("title"); //  JSON 객체에서 "title" 키에 해당하는 값을 가져와 title 필드에 저장합니다.
        this.link = itemJson.getString("link"); // JSON 객체에서 "link" 키에 해당하는 값을 가져와 link 필드에 저장합니다.
        this.image = itemJson.getString("image"); //  JSON 객체에서 "image" 키에 해당하는 값을 가져와 image 필드에 저장합니다.
        this.lprice = itemJson.getInt("lprice"); //  JSON 객체에서 "lprice" 키에 해당하는 값을 가져와 lprice 필드에 저장합니다.
    }
}