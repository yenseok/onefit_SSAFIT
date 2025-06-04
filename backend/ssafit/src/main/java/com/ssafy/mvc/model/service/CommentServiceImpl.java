package com.ssafy.mvc.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.mvc.model.dao.CommentDao;
import com.ssafy.mvc.model.dto.Comment;

@Service
public class CommentServiceImpl implements CommentService{

	@Autowired
    private CommentDao commentDao;

	// 댓글 등록
    @Override
    public void insert(Comment comment) {
        commentDao.insert(comment);
    }
    
    // 댓글 수정
    @Override
    public void update(Comment comment) {
        commentDao.update(comment);
    }

    // 댓글 삭제
    @Override
    public void delete(int commentId) {
        commentDao.delete(commentId);
    }

    // 특정 게시글의 댓글 목록 조회
    @Override
    public List<Comment> getCommentsByBoardId(int boardId) {
        return commentDao.selectByBoardId(boardId);
    }
    
    @Override
    public Comment getById(int commentId) {
        return commentDao.selectById(commentId);
    }

}
