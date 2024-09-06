package com.sparta.myselectshop.exception;

/**
 * ✅ ProductNotFoundException은 특정 상품을 찾을 수 없을 때 발생하는 사용자 정의 예외 클래스입니다.
 *
 *    ➡️ 주로 데이터베이스에서 특정 상품을 조회할 때 해당 상품이 존재하지 않으면 이 예외가 발생합니다.
 *    ➡️ RuntimeException을 상속하여 예외가 발생할 때 명시적인 예외 처리를 강제하지 않도록 설계되었습니다.
 */
public class ProductNotFoundException extends RuntimeException {

    /**
     * ✅ 예외 메시지를 포함하는 생성자입니다.
     *
     *    ➡️ 예외가 발생할 때, 해당 예외와 관련된 구체적인 메시지를 전달할 수 있습니다.
     *
     * @param message 예외 발생 시 출력할 상세 메시지입니다.
     */
    public ProductNotFoundException(String message) {
        super(message); // 부모 클래스인 RuntimeException의 생성자를 호출하여 메시지를 전달합니다.
    }
}