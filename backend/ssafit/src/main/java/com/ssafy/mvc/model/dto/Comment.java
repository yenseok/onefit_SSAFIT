package com.ssafy.mvc.model.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "댓글 DTO")
public class Comment {

	// 멤버변수 ----------------------------------------------------
	private int commentId;       // 댓글 ID
    private String content;      // 댓글 내용
    private LocalDateTime createdAt;  // 작성일
    private int userId;          // 작성자 ID
    private int boardId;         // 댓글이 달린 게시글 ID
    private String nickName;
    private String profileImage; // 작성자 프로필 사진
    
    // 생성자 ------------------------------------------------------
    // 기본 생성자
    public Comment() {
	}
    
    // 댓글 작성용 생성자 (insert 시 사용)
    public Comment(String content, int userId, int boardId) {
        this.content = content;
        this.userId = userId;
        this.boardId = boardId;
    }

    // 조회용 생성자 (select 시 사용)
	public Comment(int commentId, String content, LocalDateTime createdAt, int userId, int boardId) {
		this.commentId = commentId;
		this.content = content;
		this.createdAt = createdAt;
		this.userId = userId;
		this.boardId = boardId;
	}

	// getter, setter ---------------------------------------------
	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getBoardId() {
		return boardId;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public String getProfileImage() {
	    return profileImage;
	}

	public void setProfileImage(String profileImage) {
	    this.profileImage = profileImage;
	}
	// toString ----------------------------------------------------
	@Override
	public String toString() {
		return "Comment [commentId=" + commentId + ", content=" + content + ", createdAt=" + createdAt + ", userId="
				+ userId + ", boardId=" + boardId + "]";
	}
	
}
