package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.SignupRequestDto;
import com.sparta.myselectshop.dto.UserInfoDto;
import com.sparta.myselectshop.entity.UserRoleEnum;
import com.sparta.myselectshop.security.UserDetailsImpl;
import com.sparta.myselectshop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
     *    ➡️ 클라이언트의 요청에 대해 현재 인증된 사용자의 정보를 DTO로 반환합니다.
     *    ➡️ 반환되는 정보에는 사용자 이름과 관리자 여부가 포함됩니다.
     *
     * @param userDetails 현재 인증된 사용자의 상세 정보입니다.
     * @return UserInfoDto 현재 인증된 사용자의 정보가 담긴 DTO입니다.
     */
    @GetMapping("/user-info")
    @ResponseBody
    public UserInfoDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUser().getUsername();
        UserRoleEnum role = userDetails.getUser().getRole();
        boolean isAdmin = (role == UserRoleEnum.ADMIN);

        return new UserInfoDto(username, isAdmin);
    }
}