package com.sparta.myselectshop.dto;

import com.sparta.myselectshop.entity.Folder;
import lombok.Getter;

/**
 * ✅ FolderResponseDto 클래스는 폴더의 응답 정보를 담기 위한 데이터 전송 객체입니다.
 *
 *    ➡️ 서버가 클라이언트에게 폴더 정보를 전달할 때 사용됩니다.
 */
@Getter
public class FolderResponseDto {

    /**
     * ✅ 폴더의 고유 ID입니다.
     *
     *    ➡️ 데이터베이스에서 생성된 기본 키로, 폴더를 고유하게 식별합니다.
     */
    private Long id;

    /**
     * ✅ 폴더의 이름입니다.
     *
     *    ➡️ 폴더의 제목 또는 이름을 나타냅니다.
     */
    private String name;

    /**
     * ✅ Folder 엔티티 객체를 기반으로 FolderResponseDto를 생성합니다.
     *
     *    ➡️ Folder 엔티티 객체의 필드 값을 사용하여 DTO를 초기화합니다.
     *
     * @param folder Folder 엔티티 객체입니다. DTO의 필드 값은 이 엔티티 객체에서 가져옵니다.
     */
    public FolderResponseDto(Folder folder) {
        this.id = folder.getId(); // 엔티티의 ID를 설정합니다.
        this.name = folder.getName(); // 엔티티의 이름을 설정합니다.
    }
}
