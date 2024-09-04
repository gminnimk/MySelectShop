package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.FolderRequestDto;
import com.sparta.myselectshop.security.UserDetailsImpl;
import com.sparta.myselectshop.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ✅ FolderController 클래스는 폴더 관련 웹 요청을 처리하는 REST 컨트롤러입니다.
 *
 *    ➡️ 폴더 추가와 같은 폴더 관련 작업을 처리합니다.
 */
@RestController // REST API를 제공하는 컨트롤러로 선언합니다.
@RequestMapping("/api") // 이 컨트롤러의 모든 요청은 "/api" 경로를 기본으로 합니다.
@RequiredArgsConstructor // Lombok 어노테이션으로 생성자를 자동으로 생성합니다.
public class FolderController {

    private final FolderService folderService; // 폴더 서비스 인스턴스

    /**
     * ✅ 사용자가 요청한 폴더 이름을 기반으로 새로운 폴더를 추가합니다.
     *
     *    ➡️ 클라이언트로부터 폴더 이름 목록을 받아서 로그인한 사용자에게 폴더를 생성합니다.
     *    ➡️ 요청 본문에 포함된 `FolderRequestDto` 객체를 통해 폴더 이름 목록을 받습니다.
     *
     * @param folderRequestDto 폴더 이름 목록이 포함된 DTO 객체입니다.
     * @param userDetails 현재 인증된 사용자의 상세 정보입니다.
     */
    @PostMapping("/folders") // "/api/folders" 경로로 POST 요청을 처리합니다.
    public void addFolders(@RequestBody FolderRequestDto folderRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<String> folderNames = folderRequestDto.getFolderNames(); // DTO에서 폴더 이름 목록을 추출합니다.

        // 폴더 이름 목록과 현재 인증된 사용자를 기반으로 폴더를 추가합니다.
        folderService.addFolders(folderNames, userDetails.getUser());
    }
}