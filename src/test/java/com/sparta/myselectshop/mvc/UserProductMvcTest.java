package com.sparta.myselectshop.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.myselectshop.config.WebSecurityConfig;
import com.sparta.myselectshop.controller.ProductController;
import com.sparta.myselectshop.controller.UserController;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.entity.UserRoleEnum;
import com.sparta.myselectshop.security.UserDetailsImpl;
import com.sparta.myselectshop.service.FolderService;
import com.sparta.myselectshop.service.KakaoService;
import com.sparta.myselectshop.service.ProductService;
import com.sparta.myselectshop.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * ✅ UserProductMvcTest 클래스는 Spring MVC 컨트롤러의 웹 계층을 테스트하는 유닛 테스트 클래스입니다.
 *
 *    ➡️ MockMvc를 사용하여 컨트롤러의 HTTP 요청 및 응답을 시뮬레이션하고 검증합니다.
 *    ➡️ UserController와 ProductController의 동작을 검증하기 위한 테스트 케이스를 포함합니다.
 */
@WebMvcTest(
    controllers = {UserController.class, ProductController.class}, // 테스트할 컨트롤러 클래스들
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = WebSecurityConfig.class // 보안 설정을 제외합니다.
        )
    }
)
class UserProductMvcTest {

    private MockMvc mvc; // MockMvc 인스턴스, 컨트롤러의 HTTP 요청 및 응답을 테스트하기 위해 사용됩니다.

    private Principal mockPrincipal; // 테스트에서 사용할 사용자 Principal 객체

    @Autowired
    private WebApplicationContext context; // 웹 애플리케이션 컨텍스트

    @Autowired
    private ObjectMapper objectMapper; // JSON과 객체 간의 변환을 위한 ObjectMapper

    @MockBean
    UserService userService; // UserService의 Mock 객체

    @MockBean
    KakaoService kakaoService; // KakaoService의 Mock 객체

    @MockBean
    ProductService productService; // ProductService의 Mock 객체

    @MockBean
    FolderService folderService; // FolderService의 Mock 객체

    /**
     * ✅ 각 테스트 케이스 실행 전에 MockMvc 인스턴스를 설정합니다.
     *
     *    ➡️ WebApplicationContext를 사용하여 MockMvc를 초기화하고 Spring Security 설정을 적용합니다.
     */
    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity(new MockSpringSecurityFilter())) // Spring Security 필터 설정
            .build();
    }

    /**
     * ✅ 테스트용 사용자 Principal 객체를 생성하고 설정합니다.
     *
     *    ➡️ 테스트에서 사용할 Mock 사용자 정보를 설정합니다.
     */
    private void mockUserSetup() {
        // Mock 테스트 유저 생성
        String username = "sollertia4351";
        String password = "robbie1234";
        String email = "sollertia@sparta.com";
        UserRoleEnum role = UserRoleEnum.USER;
        User testUser = new User(username, password, email, role);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    /**
     * ✅ 로그인 페이지 요청을 테스트합니다.
     *
     *    ➡️ 로그인 페이지가 정상적으로 반환되고, 뷰 이름이 "login"인지 확인합니다.
     */
    @Test
    @DisplayName("로그인 Page")
    void test1() throws Exception {
        // when - then
        mvc.perform(get("/api/user/login-page"))
            .andExpect(status().isOk())
            .andExpect(view().name("login"))
            .andDo(print());
    }

    /**
     * ✅ 회원 가입 요청을 처리하는 테스트를 수행합니다.
     *
     *    ➡️ 회원 가입 폼 데이터가 올바르게 처리되고, 로그인 페이지로 리다이렉트되는지 확인합니다.
     */
    @Test
    @DisplayName("회원 가입 요청 처리")
    void test2() throws Exception {
        // given
        MultiValueMap<String, String> signupRequestForm = new LinkedMultiValueMap<>();
        signupRequestForm.add("username", "sollertia4351");
        signupRequestForm.add("password", "robbie1234");
        signupRequestForm.add("email", "sollertia@sparta.com");
        signupRequestForm.add("admin", "false");

        // when - then
        mvc.perform(post("/api/user/signup")
                .params(signupRequestForm)
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/api/user/login-page"))
            .andDo(print());
    }

    /**
     * ✅ 신규 관심상품 등록 요청을 테스트합니다.
     *
     *    ➡️ ProductRequestDto를 JSON 형식으로 변환하고, 이를 사용하여 POST 요청을 보내고 처리 결과를 검증합니다.
     */
    @Test
    @DisplayName("신규 관심상품 등록")
    void test3() throws Exception {
        // given
        this.mockUserSetup(); // Mock 사용자 설정
        String title = "Apple <b>아이폰</b> 14 프로 256GB [자급제]";
        String imageUrl = "https://shopping-phinf.pstatic.net/main_3456175/34561756621.20220929142551.jpg";
        String linkUrl = "https://search.shopping.naver.com/gate.nhn?id=34561756621";
        int lPrice = 959000;
        ProductRequestDto requestDto = new ProductRequestDto(
            title,
            imageUrl,
            linkUrl,
            lPrice
        );

        String postInfo = objectMapper.writeValueAsString(requestDto); // ProductRequestDto를 JSON으로 변환

        // when - then
        mvc.perform(post("/api/products")
                .content(postInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal) // Mock 사용자 Principal 설정
            )
            .andExpect(status().isOk())
            .andDo(print());
    }
}