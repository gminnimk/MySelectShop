package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.ApiUseTime;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.repository.ApiUseTimeRepository;
import com.sparta.myselectshop.security.UserDetailsImpl;
import com.sparta.myselectshop.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * ✅ ProductController 클래스는 상품 관련 요청을 처리하는 컨트롤러입니다.
 *
 *    ➡️ 상품 생성, 조회, 업데이트와 같은 작업을 처리하며, 클라이언트의 요청을 서비스 계층으로 전달합니다.
 */
@RestController // 이 클래스가 RESTful 웹 서비스의 컨트롤러임을 나타냅니다.
@RequiredArgsConstructor // Lombok 어노테이션으로, 모든 final 필드를 사용하는 생성자를 자동으로 생성합니다.
@RequestMapping("/api") // 모든 메서드는 "/api" 경로를 기본으로 합니다.
public class ProductController {

    private final ProductService productService; // 상품 관련 비즈니스 로직을 처리하는 서비스 클래스
    private final ApiUseTimeRepository apiUseTimeRepository;

    /**
     * ✅ 새로운 상품을 생성하는 API 엔드포인트입니다.
     *
     *      ➡️ 클라이언트로부터 상품 생성에 필요한 정보를 포함하는 요청 본문을 받아서 새로운 상품을 생성합니다.
     *          생성된 상품의 정보는 `ProductResponseDto` 형태로 반환됩니다.
     *
     * @param requestDto  클라이언트가 제공한 상품 생성 요청 데이터입니다. 이 DTO는 상품의 제목, 이미지 URL, 링크 URL, 가격 등을 포함합니다.
     * @param userDetails 인증된 사용자 정보입니다. 요청을 보내는 사용자의 정보를 담고 있으며, 상품 생성 시 사용자와 연관됩니다.
     * @return ProductResponseDto 생성된 상품의 정보를 담고 있는 DTO입니다. 상품의 ID, 제목, 이미지 URL, 링크 URL, 가격 등이 포함됩니다.
     */
    @PostMapping("/products")
    public ProductResponseDto createProduct(
        @RequestBody ProductRequestDto requestDto, // 상품 생성에 필요한 요청 데이터를 포함하는 DTO 객체입니다.
        @AuthenticationPrincipal UserDetailsImpl userDetails // 현재 인증된 사용자의 상세 정보를 포함하는 객체입니다.
    ) {
        // 측정 시작 시간: 상품 생성 작업의 소요 시간을 측정하기 위해 현재 시간을 기록합니다.
        long startTime = System.currentTimeMillis();

        try {
            // 상품 생성 서비스 호출: 요청 본문에서 받은 데이터를 바탕으로 상품을 생성하고, 생성된 상품의 정보를 반환합니다.
            return productService.createProduct(requestDto, userDetails.getUser());
        } finally {
            // 측정 종료 시간: 상품 생성 작업이 완료된 후의 시간을 기록합니다.
            long endTime = System.currentTimeMillis();
            // 소요 시간 계산: 종료 시간과 시작 시간의 차이를 계산하여 상품 생성 작업의 소요 시간을 측정합니다.
            long runTime = endTime - startTime;

            // 로그인 사용자 정보: 현재 인증된 사용자의 정보를 가져옵니다.
            User loginUser = userDetails.getUser();

            // API 사용 시간 기록: 사용자별 API 사용 시간을 기록하기 위해 DB에서 사용자 정보를 조회합니다.
            ApiUseTime apiUseTime = apiUseTimeRepository.findByUser(loginUser)
                .orElse(null);

            if (apiUseTime == null) {
                // 사용자의 기록이 없을 경우: 새로 생성된 API 사용 기록 객체를 생성하여 소요 시간을 기록합니다.
                apiUseTime = new ApiUseTime(loginUser, runTime);
            } else {
                // 사용자의 기록이 이미 있을 경우: 기존의 API 사용 기록에 소요 시간을 추가합니다.
                apiUseTime.addUseTime(runTime);
            }

            // API 사용 시간 로그 출력: 콘솔에 사용자의 API 사용 시간 정보를 출력하여 디버깅 및 모니터링에 사용합니다.
            System.out.println("[API Use Time] Username: " + loginUser.getUsername() + ", Total Time: " + apiUseTime.getTotalTime() + " ms");

            // API 사용 시간 DB에 저장: 업데이트된 API 사용 시간 정보를 DB에 저장하여 추후 분석 및 리포트에 사용합니다.
            apiUseTimeRepository.save(apiUseTime);
        }
    }

    /**
     * ✅ 주어진 상품 ID에 해당하는 상품의 가격을 업데이트하는 API 엔드포인트입니다.
     * <p>
     * ➡️ URL 경로에서 전달받은 상품 ID를 기반으로, 요청 본문에서 전달받은 `ProductMypriceRequestDto` 객체를 사용하여 해당 상품의
     * `myprice`를 업데이트합니다.
     *
     * @param id         업데이트할 상품의 고유 ID입니다.
     * @param requestDto 상품의 사용자 설정 가격이 포함된 DTO 객체입니다.
     * @return ProductResponseDto 업데이트된 상품 정보가 포함된 응답 DTO 객체입니다.
     * @throws IllegalAccessException 요청 처리 중 예외가 발생할 경우 던져집니다.
     */
    @PutMapping("/products/{id}")
    public ProductResponseDto updateProduct(@PathVariable Long id,
        @RequestBody ProductMypriceRequestDto requestDto) throws IllegalAccessException {
        // 서비스 계층의 updateProduct 메서드를 호출하여 상품의 사용자 설정 가격을 업데이트하고, 결과를 반환합니다.
        return productService.updateProduct(id, requestDto);
    }

    /**
     * ✅ 현재 사용자가 등록한 모든 상품의 목록을 조회하는 API 엔드포인트입니다.
     * <p>
     * ➡️ 데이터베이스에서 현재 사용자가 등록한 모든 상품을 조회하여 `ProductResponseDto` 객체 리스트로 반환합니다.
     *
     * @param page        조회할 페이지 번호입니다.
     * @param size        한 페이지에 표시할 항목 수입니다.
     * @param sortBy      정렬 기준이 되는 필드명입니다.
     * @param isAsc       오름차순 정렬 여부를 나타냅니다.
     * @param userDetails 인증된 사용자 정보입니다.
     * @return Page<ProductResponseDto> 현재 사용자가 등록한 모든 상품 정보를 담고 있는 페이지네이션된 DTO 객체 리스트입니다.
     */
    @GetMapping("/products")
    public Page<ProductResponseDto> getProducts(
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam("sortBy") String sortBy,
        @RequestParam("isAsc") boolean isAsc,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 서비스 계층의 getProducts 메서드를 호출하여 현재 사용자가 등록한 상품 목록을 조회하고 DTO 형식으로 반환합니다.
        return productService.getProducts(userDetails.getUser(), page - 1, size, sortBy, isAsc);
    }

    /**
     * ✅ 관리자가 모든 상품의 목록을 조회하는 API 엔드포인트입니다.
     * <p>
     * ➡️ 데이터베이스에서 모든 상품을 조회하여 `ProductResponseDto` 객체 리스트로 반환합니다.
     *
     * @return List<ProductResponseDto> 모든 상품 정보를 담고 있는 DTO 객체 리스트입니다.
     */
    @GetMapping("/admin/products")
    public List<ProductResponseDto> getAllProducts() {
        // 서비스 계층의 getAllProducts 메서드를 호출하여 모든 상품 목록을 조회하고 DTO 형식으로 반환합니다.
        return productService.getAllProducts();
    }

    /**
     * ✅ 특정 상품에 폴더를 추가하는 API 엔드포인트입니다.
     * <p>
     * ➡️ 클라이언트로부터 상품 ID와 폴더 ID를 받아서, 지정된 폴더에 해당 상품을 추가합니다. ➡️ 요청 본문에 포함된 `productId`와 `folderId`,
     * 그리고 인증된 사용자 정보를 기반으로 서비스 계층에서 폴더에 상품을 추가합니다.
     *
     * @param productId   폴더에 추가할 상품의 고유 ID입니다.
     * @param folderId    상품을 추가할 폴더의 고유 ID입니다.
     * @param userDetails 현재 인증된 사용자의 정보입니다.
     */
    @PostMapping("/products/{productId}/folder")
    // POST 메서드로 "/api/products/{productId}/folder" 경로에 매핑됩니다.
    public void addFolder(
        @PathVariable Long productId, // URL 경로에서 상품 ID를 추출합니다.
        @RequestParam Long folderId, // 쿼리 파라미터에서 폴더 ID를 추출합니다.
        @AuthenticationPrincipal UserDetailsImpl userDetails // 현재 인증된 사용자의 상세 정보를 추출합니다.
    ) {
        // 서비스 계층의 addFolder 메서드를 호출하여 상품을 폴더에 추가합니다.
        productService.addFolder(productId, folderId, userDetails.getUser());
    }

    /**
     * ✅ 특정 폴더에 등록된 모든 상품의 목록을 조회하는 API 엔드포인트입니다.
     *
     * ➡️ URL 경로에서 폴더 ID를 추출하고, 요청 파라미터를 사용하여 페이지 번호, 페이지 크기, 정렬 기준 및 정렬 방향을 설정합니다. ➡️ 인증된 사용자가 등록한
     * 폴더 내의 모든 상품을 조회하여 페이지네이션된 `ProductResponseDto` 객체 리스트로 반환합니다.
     *
     * @param folderId    폴더의 고유 ID입니다. 이 폴더에 등록된 상품을 조회합니다.
     * @param page        조회할 페이지 번호입니다. (0부터 시작하는 인덱스 사용)
     * @param size        한 페이지에 표시할 항목 수입니다.
     * @param sortBy      정렬 기준이 되는 필드명입니다. (예: "price", "name" 등)
     * @param isAsc       오름차순 정렬 여부를 나타냅니다. true일 경우 오름차순, false일 경우 내림차순입니다.
     * @param userDetails 현재 인증된 사용자 정보입니다. 이 정보는 요청한 사용자의 권한을 확인하는 데 사용될 수 있습니다.
     * @return Page<ProductResponseDto> 요청한 폴더에 등록된 모든 상품 정보를 담고 있는 페이지네이션된 DTO 객체 리스트입니다.
     */
    @GetMapping("/folders/{folderId}/products")
    public Page<ProductResponseDto> getProductsInFolder(
        @PathVariable Long folderId, // URL 경로에서 폴더 ID를 추출합니다.
        @RequestParam int page, // 쿼리 파라미터에서 페이지 번호를 추출합니다.
        @RequestParam int size, // 쿼리 파라미터에서 페이지 크기를 추출합니다.
        @RequestParam String sortBy, // 쿼리 파라미터에서 정렬 기준 필드명을 추출합니다.
        @RequestParam boolean isAsc, // 쿼리 파라미터에서 오름차순 정렬 여부를 추출합니다.
        @AuthenticationPrincipal UserDetailsImpl userDetails // 현재 인증된 사용자의 정보입니다.
    ) {
        // 서비스 계층의 getProductsInFolder 메서드를 호출하여 주어진 폴더 ID에 등록된 상품 목록을 조회하고,
        // 페이지네이션된 DTO 리스트를 반환합니다.
        return productService.getProductsInFolder(
            folderId,
            page - 1, // 페이지 번호는 0부터 시작하므로 -1을 합니다.
            size,
            sortBy,
            isAsc,
            userDetails.getUser() // 현재 인증된 사용자 정보를 전달합니다.
        );
    }
}