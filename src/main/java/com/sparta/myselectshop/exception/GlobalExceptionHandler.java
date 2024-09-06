package com.sparta.myselectshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ✅ GlobalExceptionHandler는 애플리케이션 전역에서 발생하는 다양한 예외를 처리하는 클래스입니다.
 *
 *    ➡️ @RestControllerAdvice 어노테이션을 사용하여, 모든 컨트롤러에서 발생하는 예외를 일관된 방식으로 처리합니다.
 *    ➡️ 발생하는 예외마다 적절한 HTTP 상태 코드와 메시지를 클라이언트에게 반환하여 오류를 명확하게 전달합니다.
 */
@RestControllerAdvice // 모든 컨트롤러에서 발생하는 예외를 처리하는 전역 예외 처리 클래스임을 선언합니다.
public class GlobalExceptionHandler {

    /**
     * ✅ IllegalArgumentException 예외를 처리하는 메서드입니다.
     *
     *    ➡️ IllegalArgumentException은 잘못된 인자가 메서드에 전달되었을 때 발생하는 예외입니다.
     *    ➡️ 해당 예외가 발생할 경우, 클라이언트에게 400 (BAD_REQUEST) 상태 코드와 예외 메시지를 반환합니다.
     *
     * @param ex 발생한 IllegalArgumentException 예외 객체입니다.
     * @return ResponseEntity<RestApiException> 에러 메시지와 상태 코드가 포함된 응답 객체입니다.
     */
    @ExceptionHandler({IllegalArgumentException.class}) // IllegalArgumentException이 발생하면 이 메서드가 호출됩니다.
    public ResponseEntity<RestApiException> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        // 예외 메시지와 HTTP 상태 400을 담은 RestApiException 객체를 생성하여 응답으로 반환합니다.
        RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
            restApiException, // HTTP 응답 본문에 포함될 RestApiException 객체
            HttpStatus.BAD_REQUEST // HTTP 응답 상태 코드를 400으로 설정합니다.
        );
    }

    /**
     * ✅ NullPointerException 예외를 처리하는 메서드입니다.
     *
     *    ➡️ NullPointerException은 참조 대상이 null일 때 발생하는 예외입니다.
     *    ➡️ 해당 예외가 발생할 경우, 클라이언트에게 404 (NOT_FOUND) 상태 코드와 예외 메시지를 반환합니다.
     *
     * @param ex 발생한 NullPointerException 예외 객체입니다.
     * @return ResponseEntity<RestApiException> 에러 메시지와 상태 코드가 포함된 응답 객체입니다.
     */
    @ExceptionHandler({NullPointerException.class}) // NullPointerException이 발생하면 이 메서드가 호출됩니다.
    public ResponseEntity<RestApiException> nullPointerExceptionHandler(NullPointerException ex) {
        // 예외 메시지와 HTTP 상태 404를 담은 RestApiException 객체를 생성하여 응답으로 반환합니다.
        RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(
            restApiException, // HTTP 응답 본문에 포함될 RestApiException 객체
            HttpStatus.NOT_FOUND // HTTP 응답 상태 코드를 404로 설정합니다.
        );
    }

    /**
     * ✅ ProductNotFoundException 예외를 처리하는 메서드입니다.
     *
     *    ➡️ ProductNotFoundException은 특정 상품을 찾을 수 없을 때 발생하는 사용자 정의 예외입니다.
     *    ➡️ 해당 예외가 발생할 경우, 클라이언트에게 404 (NOT_FOUND) 상태 코드와 예외 메시지를 반환합니다.
     *
     * @param ex 발생한 ProductNotFoundException 예외 객체입니다.
     * @return ResponseEntity<RestApiException> 에러 메시지와 상태 코드가 포함된 응답 객체입니다.
     */
    @ExceptionHandler({ProductNotFoundException.class}) // ProductNotFoundException이 발생하면 이 메서드가 호출됩니다.
    public ResponseEntity<RestApiException> notFoundProductExceptionHandler(ProductNotFoundException ex) {
        // 예외 메시지와 HTTP 상태 404를 담은 RestApiException 객체를 생성하여 응답으로 반환합니다.
        RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(
            restApiException, // HTTP 응답 본문에 포함될 RestApiException 객체
            HttpStatus.NOT_FOUND // HTTP 응답 상태 코드를 404로 설정합니다.
        );
    }
}