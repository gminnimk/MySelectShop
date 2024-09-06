package com.sparta.myselectshop.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * ✅ RestApiException은 API 요청 중 발생한 예외 정보를 담는 클래스입니다.
 *
 *    ➡️ 발생한 에러 메시지와 HTTP 상태 코드를 포함하여 클라이언트에게 응답할 때 사용됩니다.
 */
@RequiredArgsConstructor
@Getter
@AllArgsConstructor
public class RestApiException {

    /**
     * ✅ 에러 메시지를 나타냅니다.
     *
     *    ➡️ 예외 발생 시 클라이언트에게 전달될 메시지입니다.
     */
    private String errorMessage;

    /**
     * ✅ HTTP 상태 코드를 나타냅니다.
     *
     *    ➡️ 예외 상황에 맞는 HTTP 응답 상태 코드를 설정합니다.
     */
    private int statusCode;
}
