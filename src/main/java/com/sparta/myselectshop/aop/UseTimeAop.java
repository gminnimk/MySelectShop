package com.sparta.myselectshop.aop;

import com.sparta.myselectshop.entity.ApiUseTime;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.repository.ApiUseTimeRepository;
import com.sparta.myselectshop.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * ✅ UseTimeAop 클래스는 API 호출의 실행 시간을 측정하고 기록하는 AOP (Aspect-Oriented Programming) 클래스입니다.
 *
 *    ➡️ 주어진 포인트컷에 해당하는 메서드 호출의 실행 시간을 측정하고, 로그인한 사용자에 대한 API 사용 시간을 데이터베이스에 저장합니다.
 */
@Slf4j(topic = "UseTimeAop") // 로그 출력을 위한 Lombok 어노테이션입니다.
@Aspect // AOP의 Aspect를 정의하는 어노테이션입니다.
@Component // Spring의 컴포넌트로 등록됩니다.
@RequiredArgsConstructor // 필수 생성자를 자동으로 생성해줍니다.
public class UseTimeAop {

    private final ApiUseTimeRepository apiUseTimeRepository; // API 사용 시간을 저장할 레포지토리입니다.

    /**
     * ✅ ProductController 클래스의 모든 메서드를 대상으로 하는 포인트컷입니다.
     */
    @Pointcut("execution(* com.sparta.myselectshop.controller.ProductController.*(..))")
    private void product() {}

    /**
     * ✅ FolderController 클래스의 모든 메서드를 대상으로 하는 포인트컷입니다.
     */
    @Pointcut("execution(* com.sparta.myselectshop.controller.FolderController.*(..))")
    private void folder() {}

    /**
     * ✅ NaverApiController 클래스의 모든 메서드를 대상으로 하는 포인트컷입니다.
     */
    @Pointcut("execution(* com.sparta.myselectshop.naver.controller.NaverApiController.*(..))")
    private void naver() {}

    /**
     * ✅ 지정된 포인트컷에 해당하는 메서드의 실행 시간을 측정합니다.
     *
     *    ➡️ 메서드 실행 전과 후의 시간을 기록하여 실행 시간을 계산하고, 이를 데이터베이스에 저장합니다.
     *
     * @param joinPoint AOP 프록시가 호출한 메서드의 정보를 담고 있는 객체입니다.
     * @return 메서드 실행 결과 객체입니다.
     * @throws Throwable 메서드 실행 중 발생할 수 있는 예외를 던집니다.
     */
    @Around("product() || folder() || naver()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        // 측정 시작 시간 기록
        long startTime = System.currentTimeMillis();

        try {
            // 핵심 비즈니스 로직 수행
            Object output = joinPoint.proceed();
            return output;
        } finally {
            // 측정 종료 시간 기록
            long endTime = System.currentTimeMillis();
            // 수행 시간 계산 (종료 시간 - 시작 시간)
            long runTime = endTime - startTime;

            // 로그인한 사용자의 정보를 확인하고, API 사용 시간 기록
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal().getClass() == UserDetailsImpl.class) {
                // 로그인한 사용자 정보 가져오기
                UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
                User loginUser = userDetails.getUser();

                // 사용자에 대한 API 사용 시간 조회 및 업데이트
                ApiUseTime apiUseTime = apiUseTimeRepository.findByUser(loginUser).orElse(null);
                if (apiUseTime == null) {
                    // 기록이 없으면 새로 생성
                    apiUseTime = new ApiUseTime(loginUser, runTime);
                } else {
                    // 기록이 있으면 사용 시간을 추가
                    apiUseTime.addUseTime(runTime);
                }

                // 로그에 API 사용 시간 기록
                log.info("[API Use Time] Username: " + loginUser.getUsername() + ", Total Time: " + apiUseTime.getTotalTime() + " ms");
                // 데이터베이스에 API 사용 시간 저장
                apiUseTimeRepository.save(apiUseTime);
            }
        }
    }
}
