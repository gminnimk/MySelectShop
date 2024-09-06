package com.sparta.myselectshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ✅ GlobalExceptionHandler는 애플리케이션 전역에서 발생하는 예외를 처리하는 클래스입니다.
 *
 *    ➡️ @RestControllerAdvice 어노테이션을 사용하여 모든 컨트롤러에서 발생하는 예외를 일괄적으로 처리합니다.
 *    ➡️ 예외가 발생했을 때, 개별 컨트롤러에서 예외 처리를 하지 않더라도 이 클래스에서 정의된 핸들러가 자동으로 호출됩니다.
 *    ➡️ 현재 IllegalArgumentException에 대한 예외 처리를 정의하고 있습니다.
 */
@RestControllerAdvice // 모든 컨트롤러에서 발생하는 예외를 처리하는 전역 예외 처리 클래스임을 선언합니다.
public class GlobalExceptionHandler {

    /**
     * ✅ IllegalArgumentException 예외를 처리하는 메서드입니다.
     *
     *    ➡️ IllegalArgumentException은 주로 잘못된 인자가 전달되었을 때 발생하는 예외입니다.
     *    ➡️ 이 메서드는 해당 예외가 발생했을 때, 에러 메시지와 함께 HTTP 상태 코드 400 (BAD_REQUEST)을 반환합니다.
     *
     * @param ex 처리할 IllegalArgumentException 예외 객체입니다.
     * @return ResponseEntity<RestApiException> 에러 메시지와 상태 코드를 포함한 응답 객체입니다.
     */
    @ExceptionHandler({IllegalArgumentException.class}) // IllegalArgumentException 발생 시 이 메서드가 호출됩니다.
    public ResponseEntity<RestApiException> handleException(IllegalArgumentException ex) {
        // 예외 메시지와 HTTP 상태 코드를 담은 RestApiException 객체를 생성합니다.
        RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());

        // RestApiException 객체와 HTTP 상태 400 (BAD_REQUEST)을 응답으로 반환합니다.
        return new ResponseEntity<>(
            restApiException, // HTTP 응답 본문에 포함될 내용입니다.
            HttpStatus.BAD_REQUEST // HTTP 응답 상태 코드를 400으로 설정합니다.
        );
    }
}