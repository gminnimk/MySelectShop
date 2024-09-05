package com.sparta.myselectshop.mvc;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

/**
 * ✅ MockSpringSecurityFilter 클래스는 테스트 환경에서 Spring Security 컨텍스트에
 *    인증 정보를 설정하기 위한 가짜(Security Mock) 필터입니다.
 *
 *    ➡️ 실제 인증 과정을 거치지 않고, 테스트 중에 SecurityContext에 인증 객체를 설정할 수 있게 도와줍니다.
 *    ➡️ 주로 인증이 필요한 테스트에서 사용되며, 사용자가 직접 요청을 통해 인증된 것처럼 처리합니다.
 */
public class MockSpringSecurityFilter implements Filter {

    /**
     * ✅ 필터 초기화 메서드입니다.
     * ➡️ 현재 구현에서는 초기화할 내용이 없으므로 비워두었습니다.
     *
     * @param filterConfig 필터 설정 정보 (사용되지 않음)
     */
    @Override
    public void init(FilterConfig filterConfig) {}

    /**
     * ✅ 실제 필터링 작업을 처리하는 메서드입니다.
     *
     *    ➡️ 요청으로부터 `UserPrincipal`을 가져와 이를 `SecurityContext`에 설정합니다.
     *    ➡️ 이를 통해 Spring Security에서 인증된 사용자 정보를 사용할 수 있게 됩니다.
     *
     * @param req  클라이언트로부터 들어온 서블릿 요청 객체입니다.
     * @param res  클라이언트로 응답할 서블릿 응답 객체입니다.
     * @param chain 필터 체인 객체로, 다음 필터로 요청을 전달합니다.
     * @throws IOException      I/O 처리 중 발생할 수 있는 예외
     * @throws ServletException 서블릿 처리 중 발생할 수 있는 예외
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        // 요청으로부터 사용자 인증 정보(UserPrincipal)를 가져와 SecurityContext에 설정합니다.
        SecurityContextHolder.getContext()
            .setAuthentication((Authentication) ((HttpServletRequest) req).getUserPrincipal());

        // 필터 체인에 따라 다음 필터로 요청을 전달합니다.
        chain.doFilter(req, res);
    }

    /**
     * ✅ 필터 종료 시 호출되는 메서드입니다.
     * ➡️ 필터가 종료될 때 SecurityContext를 비워서, 인증 정보를 삭제합니다.
     */
    @Override
    public void destroy() {
        // SecurityContext를 초기화하여 인증 정보를 제거합니다.
        SecurityContextHolder.clearContext();
    }
}