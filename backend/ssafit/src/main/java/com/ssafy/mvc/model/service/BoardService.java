package com.ssafy.mvc.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.mvc.model.dto.Board;
import com.ssafy.mvc.model.dto.BoardSearchCondition;

public interface BoardService {

	// 게시글 목록 조회
    List<Board> getAllLatest(BoardSearchCondition condition);       // 최신순
    List<Board> getAllPopular(BoardSearchCondition condition);      // 인기순
    List<Board> getMyBoards(int userId, BoardSearchCondition condition);  // 내 게시글
    
    // 게시글 검색 (제목, 내용, 작성자)
    List<Board> search(BoardSearchCondition condition);
    
    // 게시글 상세 조회
    Board getBoardDetail(int boardId);  // 상세조회 + 조회수 증가 포함
    
    // 게시글 작성, 수정, 삭제
    void write(Board board, List<MultipartFile> imageFiles);  // 새 이미지 업로드 포함
    void update(Board board, List<MultipartFile> imageFiles);  // 기존 이미지 유지/삭제 + 새 이미지 추가
    void delete(int boardId);
    
    // 좋아요
    void like(int boardId, int userId);  // 좋아요 선택
    void unlike(int boardId, int userId);  // 좋아요 취소
    boolean isLiked(int boardId, int userId); // 좋아요 여부 확인
    
    // 조회수 증가
    void increaseViewCount(int boardId);
    
    // 댓글 수 관련 (실시간 COUNT)
    int countComments(int boardId);
}
