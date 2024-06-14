package com.sparta.myselectshop.dto;

import lombok.Getter;
import lombok.Setter;


// ProductMypriceRequestDto라는 이름의 Data Transfer Object(DTO) 클래스를 정의하고 있습니다.
// DTO는 주로 데이터를 캡슐화하여 다른 계층 간에 전달하기 위해 사용됩니다



/*
ProductMypriceRequestDto 클래스는 단순히 myprice라는 하나의 정수형 필드를 가진 DTO입니다.
Lombok의 @Getter와 @Setter 애노테이션을 사용하여 자동으로 이 필드에 대한 getter와 setter 메서드를 생성합니다.

이 클래스는 주로 클라이언트가 제품의 희망 가격을 서버에 전달할 때 사용됩니다.
예를 들어, 사용자가 특정 제품에 대해 희망하는 가격을 입력하고 이를 서버에 전송하면,
서버는 이 데이터를 ProductMypriceRequestDto 객체로 받아서 처리할 수 있습니다.
 */




// @Getter와 @Setter: Lombok 애노테이션으로, 이 클래스에 대해 getter와 setter 메서드를 자동으로 생성해 줍니다.
// 즉, getMyprice()와 setMyprice(int myprice) 메서드가 자동으로 생성됩니다.

@Getter
@Setter
public class ProductMypriceRequestDto {
    private int myprice; // 이 클래스는 하나의 필드 myprice를 가지고 있으며, 이는 int 타입입니다.
}