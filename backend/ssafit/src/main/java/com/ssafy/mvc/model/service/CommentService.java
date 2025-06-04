package com.ssafy.mvc.model.service;

import java.util.List;

import com.ssafy.mvc.model.dto.Comment;

public interface CommentService {

	// 댓글 등록
    void insert(Comment comment);

    // 댓글 수정
    void update(Comment comment);

    // 댓글 삭제
    void delete(int commentId);

    // 특정 게시글의 댓글 목록 조회
    List<Comment> getCommentsByBoardId(int boardId);
    
    Comment getById(int commentId);

}
