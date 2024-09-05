package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
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

    /**
     * ✅ 새로운 상품을 생성하는 API 엔드포인트입니다.
     *
     *      ➡️ 클라이언트로부터 상품 정보를 포함하는 요청 본문을 받아서 상품을 생성하고, 생성된 상품의 정보를 반환합니다.
     *
     * @param requestDto  클라이언트가 제공한 상품 생성 요청 데이터입니다.
     * @param userDetails 인증된 사용자 정보입니다.
     * @return ProductResponseDto 생성된 상품의 정보가 담긴 DTO입니다.
     */
    @PostMapping("/products") // POST 메서드로 "/api/products" 경로에 매핑됩니다.
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 서비스 계층의 createProduct 메서드를 호출하여 상품을 생성하고, 결과를 반환합니다.
        return productService.createProduct(requestDto, userDetails.getUser());
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