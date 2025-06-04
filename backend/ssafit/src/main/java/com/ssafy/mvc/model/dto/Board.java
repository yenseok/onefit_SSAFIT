package com.ssafy.mvc.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 DTO")
public class Board {

	// 멤버변수 ----------------------------------------------------
	private int boardId;  // 게시글 ID
    private String title;  // 게시글 제목
    private String content;  // 내용
    private String thumbnail;  // 대표 이미지 URL
    
    // LocalDateTime : DB의 DATETIME과 잘 매핑되는 자료형
    private LocalDateTime createdAt;  // 작성일
    private int userId;  // 작성자
    
    private int viewCount;  // 조회수
    private int likeCount;  // 좋아요 수
    private int commentCount;  // 댓글 수
    
    private List<Comment> comments;  // 댓글 목록
    private List<String> imageUrls;  // 이미지 URL 목록
    private List<String> deleteImageUrls; // 게시글 수정 시 삭제할 이미지 목록
    
    private String nickName; // 작성자 이름
	private String profileImage; // 작성자 프로필 사진
    
    // 생성자 ------------------------------------------------------
    // 기본 생성자
    public Board() {
	}
    
    // 게시글 작성 시 사용하는 생성자
    public Board(String title, String content, int userId, String thumbnail, List<String> imageUrls) {
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.thumbnail = thumbnail;
        this.imageUrls = imageUrls;
    }
    
    // 조회할 때 사용하는 생성자
    public Board(int boardId, String title, String content, LocalDateTime createdAt,
            int userId, int viewCount, int likeCount, int commentCount, String thumbnail, List<String> imageUrls) {
	   this.boardId = boardId;
	   this.title = title;
	   this.content = content;
	   this.createdAt = createdAt;
	   this.userId = userId;
	   this.viewCount = viewCount;
	   this.likeCount = likeCount;
	   this.commentCount = commentCount;
	   this.thumbnail = thumbnail;
	   this.imageUrls = imageUrls;
	}

    // getter, setter ---------------------------------------------
	public int getBoardId() {
		return boardId;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	
	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
    
    public List<String> getDeleteImageUrls() {
        return deleteImageUrls;
    }

    public void setDeleteImageUrls(List<String> deleteImageUrls) {
        this.deleteImageUrls = deleteImageUrls;
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
		return "Board [boardId=" + boardId + ", title=" + title + ", content=" + content + ", thumbnail=" + thumbnail
				+ ", createdAt=" + createdAt + ", userId=" + userId + ", viewCount=" + viewCount + ", likeCount="
				+ likeCount + ", commentCount=" + commentCount + ", comments=" + comments + ", imageUrls=" + imageUrls
				+ ", deleteImageUrls=" + deleteImageUrls + "]";
	}

	
}
