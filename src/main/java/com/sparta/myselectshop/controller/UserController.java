package com.sparta.myselectshop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.myselectshop.jwt.JwtUtil;
import com.sparta.myselectshop.service.KakaoService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import com.sparta.myselectshop.dto.SignupRequestDto;
import com.sparta.myselectshop.dto.UserInfoDto;
import com.sparta.myselectshop.entity.UserRoleEnum;
import com.sparta.myselectshop.security.UserDetailsImpl;
import com.sparta.myselectshop.service.FolderService;
import com.sparta.myselectshop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * ✅ UserController 클래스는 사용자 관련 웹 요청을 처리하는 컨트롤러입니다.
 *
 *    ➡️ 사용자 가입, 로그인 페이지 렌더링, 사용자 정보 조회 등의 기능을 제공합니다.
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final FolderService folderService;
    private final KakaoService kakaoService;


    /**
     * ✅ 로그인 페이지를 반환합니다.
     *
     *    ➡️ 클라이언트의 로그인 요청에 대해 로그인 페이지를 반환합니다.
     *
     * @return 로그인 페이지의 뷰 이름입니다.
     */
    @GetMapping("/user/login-page")
    public String loginPage() {
        return "login";
    }

    /**
     * ✅ 회원 가입 페이지를 반환합니다.
     *
     *    ➡️ 클라이언트의 회원 가입 요청에 대해 회원 가입 페이지를 반환합니다.
     *
     * @return 회원 가입 페이지의 뷰 이름입니다.
     */
    @GetMapping("/user/signup")
    public String signupPage() {
        return "signup";
    }

    /**
     * ✅ 회원 가입 요청을 처리합니다.
     *
     *    ➡️ 클라이언트로부터 회원 가입 정보를 받아서 처리하며, 유효성 검사를 수행합니다.
     *    ➡️ 유효성 검사 오류가 발생하면 회원 가입 페이지로 리다이렉트하고, 오류 메시지를 로그에 기록합니다.
     *
     * @param requestDto 회원 가입에 필요한 데이터가 담긴 DTO입니다.
     * @param bindingResult 유효성 검사 결과를 담고 있는 객체입니다.
     * @return 회원 가입 처리 후 로그인 페이지로 리다이렉트합니다.
     */
    @PostMapping("/user/signup")
    public String signup(@Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return "redirect:/api/user/signup";
        }

        userService.signup(requestDto);

        return "redirect:/api/user/login-page";
    }

    /**
     * ✅ 현재 인증된 사용자의 정보를 반환합니다.
     *
     *    ➡️ 클라이언트의 요청에 대해 현재 인증된 사용자의 정보를 JSON 형식으로 반환합니다.
     *    ➡️ 반환되는 정보에는 사용자의 이름과 관리자인지 여부가 포함됩니다.
     *
     * @param userDetails 현재 인증된 사용자의 상세 정보가 담긴 객체입니다.
     *                    이 객체는 Spring Security에서 인증된 사용자 정보를 제공합니다.
     * @return UserInfoDto 현재 인증된 사용자의 정보가 담긴 DTO 객체입니다.
     *                     반환되는 DTO 객체는 사용자의 이름과 관리자 여부를 포함합니다.
     */
    @GetMapping("/user-info")
    @ResponseBody
    public UserInfoDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUser().getUsername(); // 인증된 사용자의 사용자 이름을 가져옵니다.
        UserRoleEnum role = userDetails.getUser().getRole(); // 인증된 사용자의 역할을 가져옵니다.
        boolean isAdmin = (role == UserRoleEnum.ADMIN); // 사용자가 관리자 역할인지 여부를 확인합니다.

        // 사용자 이름과 관리자 여부를 담은 DTO 객체를 반환합니다.
        return new UserInfoDto(username, isAdmin);
    }

    /**
     * ✅ 현재 인증된 사용자의 폴더 목록을 조회하고, 이를 뷰에 전달합니다.
     *
     *    ➡️ 클라이언트의 요청에 대해 현재 인증된 사용자의 폴더 정보를 가져와서 `Model` 객체에 추가합니다.
     *    ➡️ 반환되는 뷰는 "index" 템플릿의 `#fragment` 부분입니다. 이 부분은 Thymeleaf 템플릿에서 사용자 폴더 목록을 표시하는 데 사용됩니다.
     *
     * @param model Spring MVC의 `Model` 객체로, 뷰에 데이터를 전달하는 데 사용됩니다.
     *              여기서는 사용자 폴더 목록을 "folders"라는 이름으로 모델에 추가합니다.
     * @param userDetails 현재 인증된 사용자의 상세 정보가 담긴 `UserDetailsImpl` 객체입니다.
     *                    이 객체를 통해 인증된 사용자의 정보를 가져오고, 폴더 목록을 조회하는 데 사용됩니다.
     * @return "index :: #fragment" 문자열은 Thymeleaf 템플릿의 특정 부분을 반환합니다.
     *         이 문자열은 `index.html` 템플릿의 `#fragment` 부분을 렌더링하여 사용자 폴더 목록을 표시합니다.
     */
    @GetMapping("/user-folder")
    public String getUserInfo(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 현재 인증된 사용자의 폴더 목록을 조회하여 모델에 추가합니다.
        model.addAttribute("folders", folderService.getFolders(userDetails.getUser()));

        // "index" 템플릿의 "#fragment" 부분을 반환합니다.
        return "index :: #fragment";
    }

    /**
     * ✅ 카카오 로그인 콜백을 처리하고 JWT 토큰을 쿠키에 저장합니다.
     *
     *    ➡️ 클라이언트가 카카오 로그인 성공 후 인가 코드를 전달하면, 이 코드를 사용하여
     *        카카오 인증 서버에서 액세스 토큰을 받고, 이를 통해 사용자를 인증한 후 JWT 토큰을 생성합니다.
     *    ➡️ 생성된 JWT 토큰은 쿠키로 만들어서 클라이언트에게 반환되며, 이후 요청에서 인증에 사용됩니다.
     *
     * @param code 카카오 인증 서버로부터 받은 인가 코드입니다.
     * @param response 클라이언트에게 응답을 보내기 위해 사용되는 `HttpServletResponse` 객체입니다.
     *                 이 객체를 통해 JWT 토큰을 담은 쿠키를 클라이언트에게 전달합니다.
     * @return 홈 페이지로 리다이렉트하는 문자열입니다.
     * @throws JsonProcessingException JSON 처리 중 발생할 수 있는 예외입니다.
     */
    @GetMapping("/user/kakao/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        // ✅ 카카오 서버로부터 받은 인가 코드를 Service로 전달하여 인증을 처리하고, JWT 토큰을 반환받습니다.
        String token = kakaoService.kakaoLogin(code);

        // ✅ 반환된 JWT 토큰을 쿠키로 생성합니다.
        //    ➡️ 쿠키의 이름은 'Authorization'이며, 토큰의 'Bearer ' 부분을 제외한 값을 저장합니다.
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token.substring(7));
        cookie.setPath("/"); // 쿠키의 경로를 설정하여 모든 요청에서 쿠키가 전송되도록 합니다.
        response.addCookie(cookie); // 쿠키를 HTTP 응답에 추가하여 클라이언트에게 전송합니다.

        // ✅ 홈 페이지로 리다이렉트합니다.
        return "redirect:/";
    }
}