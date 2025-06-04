package com.ssafy.mvc.model.dao;

import java.util.List;

public interface BoardImageDao {

	// 이미지 등록 (image_order도 함께 저장)
    void insert(int boardId, String imageUrl, boolean isThumbnail, int imageOrder);

    // 게시글 ID 기준 이미지 전체 삭제
    void deleteByBoardId(int boardId);

    // 게시글 ID 기준 이미지 목록 조회 (정렬 포함)
    List<String> selectByBoardId(int boardId);

    // 게시글 ID와 이미지 URL 기준으로 특정 이미지 삭제
    void deleteByBoardIdAndImageUrl(int boardId, String imageUrl);

    // 이미지 순서 변경
    void updateImageOrder(int boardId, String imageUrl, int imageOrder);

    // 대표 이미지 여부 설정
    void updateThumbnailFlag(int boardId, String imageUrl, boolean isThumbnail);
}
