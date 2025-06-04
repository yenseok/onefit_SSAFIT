package com.ssafy.mvc.model.dao;

public interface BoardLikeDao {

	// 좋아요 등록
    void insert(int boardId, int userId);

    // 좋아요 취소
    void delete(int boardId, int userId);

    // 좋아요 여부 확인 (0 또는 1 반환)
    int isLiked(int boardId, int userId);

}
