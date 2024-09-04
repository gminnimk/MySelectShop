package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.FolderResponseDto;
import com.sparta.myselectshop.entity.Folder;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * ✅ FolderService 클래스는 사용자와 관련된 폴더를 관리하는 서비스 클래스입니다.
 *
 *    ➡️ 사용자의 폴더를 추가하거나 조회하는 기능을 제공합니다.
 *    ➡️ 폴더가 중복되는지 검사하고, 폴더 정보를 `FolderRepository`를 통해 데이터베이스와 연동합니다.
 */
@Service // 이 클래스가 Spring의 서비스 컴포넌트임을 나타냅니다.
@RequiredArgsConstructor // Lombok 어노테이션으로 생성자를 자동으로 생성합니다.
public class FolderService {

    private final FolderRepository folderRepository; // 폴더 정보를 처리하기 위한 리포지토리입니다.

    /**
     * ✅ 로그인한 사용자의 폴더를 추가합니다.
     *
     *    ➡️ 입력으로 받은 폴더 이름 목록을 기반으로, 사용자가 이미 생성한 폴더들을 조회합니다.
     *    ➡️ 중복된 폴더 이름이 없는 경우에만 새로운 폴더를 생성하고, 데이터베이스에 저장합니다.
     *    ➡️ 폴더 이름이 중복된 경우에는 `IllegalArgumentException` 예외를 발생시킵니다.
     *
     * @param folderNames 사용자가 추가하려는 폴더 이름 목록입니다.
     * @param user 폴더를 추가하는 사용자의 정보입니다.
     */
    public void addFolders(List<String> folderNames, User user) {

        // 입력된 폴더 이름을 기준으로 사용자가 이미 생성한 폴더를 조회합니다.
        List<Folder> existFolderList = folderRepository.findAllByUserAndNameIn(user, folderNames);

        List<Folder> folderList = new ArrayList<>();

        for (String folderName : folderNames) {
            // 이미 존재하는 폴더가 아닌 경우만 새 폴더를 생성합니다.
            if (!isExistFolderName(folderName, existFolderList)) {
                Folder folder = new Folder(folderName, user);
                folderList.add(folder);
            } else {
                throw new IllegalArgumentException("폴더명이 중복되었습니다."); // 중복된 폴더 이름이 있을 경우 예외 발생
            }
        }

        folderRepository.saveAll(folderList); // 새로 생성된 폴더를 데이터베이스에 저장합니다.
    }

    /**
     * ✅ 폴더 이름이 이미 존재하는지 확인합니다.
     *
     *    ➡️ 주어진 폴더 이름이 기존 폴더 목록에 포함되어 있는지 검사합니다.
     *
     * @param folderName 확인할 폴더 이름입니다.
     * @param existFolderList 기존 폴더 목록입니다.
     * @return 폴더 이름이 기존 폴더 목록에 존재하면 `true`, 그렇지 않으면 `false`를 반환합니다.
     */
    private Boolean isExistFolderName(String folderName, List<Folder> existFolderList) {
        // 기존 폴더 리스트에서 해당 폴더 이름이 존재하는지 확인합니다.
        for (Folder existFolder : existFolderList) {
            if (folderName.equals(existFolder.getName())) {
                return true; // 폴더 이름이 이미 존재하면 true 반환
            }
        }
        return false; // 폴더 이름이 존재하지 않으면 false 반환
    }

    /**
     * ✅ 로그인한 사용자가 등록한 모든 폴더를 조회합니다.
     *
     *    ➡️ 사용자가 등록한 모든 폴더를 조회하여 `FolderResponseDto` 목록으로 변환합니다.
     *
     * @param user 폴더를 조회할 사용자의 정보입니다.
     * @return 사용자가 등록한 폴더 정보가 담긴 `FolderResponseDto` 리스트입니다.
     */
    public List<FolderResponseDto> getFolders(User user) {
        List<Folder> folderList = folderRepository.findAllByUser(user); // 사용자가 등록한 모든 폴더를 조회합니다.
        List<FolderResponseDto> responseDtoList = new ArrayList<>();

        for (Folder folder : folderList) {
            responseDtoList.add(new FolderResponseDto(folder)); // 폴더를 DTO로 변환하여 리스트에 추가합니다.
        }

        return responseDtoList; // 폴더 DTO 리스트를 반환합니다.
    }
}