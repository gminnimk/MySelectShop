package com.sparta.myselectshop.scheduler;

import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.naver.dto.ItemDto;
import com.sparta.myselectshop.naver.service.NaverApiService;
import com.sparta.myselectshop.repository.ProductRepository;
import com.sparta.myselectshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ✅ Scheduler 클래스는 정기적인 작업을 스케줄링하여 자동으로 실행하는 컴포넌트입니다.
 *
 *    ➡️ 주기적으로 실행되는 작업을 설정하고 관리합니다.
 *    ➡️ 이 클래스는 NAVER API를 사용하여 제품 가격을 업데이트하는 작업을 수행합니다.
 */
@Slf4j(topic = "Scheduler") // SLF4J를 사용하여 로그를 기록합니다.
@Component // Spring의 컴포넌트로 등록되어 DI(Dependency Injection) 대상이 됩니다.
@RequiredArgsConstructor // final 필드를 생성자 주입 방식으로 초기화합니다.
public class Scheduler {

    private final NaverApiService naverApiService; // NAVER API와 상호작용하는 서비스
    private final ProductService productService; // 제품 정보를 처리하는 서비스
    private final ProductRepository productRepository; // 제품 정보를 데이터베이스에서 조회하는 리포지토리

    // 초, 분, 시, 일, 월, 주 순서
    @Scheduled(cron = "0 0 1 * * *") // 매일 새벽 1시에 실행됩니다.
    public void updatePrice() throws InterruptedException {
        log.info("가격 업데이트 실행"); // 작업 시작 로그

        // 데이터베이스에서 모든 제품 목록을 조회합니다.
        List<Product> productList = productRepository.findAll();

        for (Product product : productList) {
            // NAVER API 호출 제한을 피하기 위해 1초 대기합니다.
            TimeUnit.SECONDS.sleep(1);

            // 제품의 제목을 기반으로 NAVER API에서 검색을 실행합니다.
            String title = product.getTitle();
            List<ItemDto> itemDtoList = naverApiService.searchItems(title);

            if (!itemDtoList.isEmpty()) {
                ItemDto itemDto = itemDtoList.get(0); // 검색 결과 중 첫 번째 아이템을 선택합니다.

                // 제품의 ID를 사용하여 제품 정보를 업데이트합니다.
                Long id = product.getId();
                try {
                    productService.updateBySearch(id, itemDto); // 제품 정보를 업데이트하는 서비스 메서드 호출
                } catch (Exception e) {
                    log.error(id + " : " + e.getMessage()); // 업데이트 중 오류 발생 시 로그 기록
                }
            }
        }
    }
}