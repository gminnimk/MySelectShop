package com.sparta.myselectshop.dto;

import lombok.Getter;
import java.util.List;

/**
 * ✅ FolderRequestDto 클래스는 폴더 관련 요청 정보를 담기 위한 데이터 전송 객체입니다.
 *
 *    ➡️ 클라이언트가 서버로 폴더 생성 요청을 보낼 때, 폴더 이름 목록을 전달하는 데 사용됩니다.
 */
@Getter
public class FolderRequestDto {

    /**
     * ✅ 폴더 이름 목록입니다.
     *
     *    ➡️ 클라이언트가 생성하려는 폴더의 이름들을 리스트 형태로 담고 있습니다.
     *    ➡️ 이 리스트는 서버에서 폴더 생성 작업을 수행하는 데 필요한 정보를 제공합니다.
     */
    List<String> folderNames;
}