package com.ssafy.mvc.model.dao;

import java.util.List;

import com.ssafy.mvc.model.dto.Board;
import com.ssafy.mvc.model.dto.BoardSearchCondition;


public interface BoardDao {
	
	// 게시글 목록 조회 (최신순, 인기순 포함)
    List<Board> selectAll(BoardSearchCondition condition); // 정렬 조건은 내부적으로 처리

    // 내 게시글 목록
    List<Board> selectByUserId(int userId, BoardSearchCondition condition);

    // 검색
    List<Board> search(BoardSearchCondition condition);

    // 게시글 상세 조회
    Board select(int boardId);

    // 게시글 등록 / 수정 / 삭제
    void insert(Board board);
    void update(Board board);  // 이미지 갱신까지 처리
    void delete(int boardId);

    // 좋아요 수 증가 / 감소 (추후에 BaordLikeDao와 트랜잭션 처리해도 좋을듯)
    void increaseLikeCount(int boardId);
    void decreaseLikeCount(int boardId);
    
    // 조회수 증가
    void increaseViewCount(int boardId);

    // 댓글 수 실시간 조회 (SELECT COUNT)
    int countComments(int boardId);
}
