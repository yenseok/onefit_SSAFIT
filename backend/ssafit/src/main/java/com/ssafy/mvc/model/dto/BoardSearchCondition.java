package com.ssafy.mvc.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 검색 DTO")
public class BoardSearchCondition {

	// 멤버변수 ----------------------------------------------------
	private String keyword;      // 검색 키워드
    private String type;         // 검색 타입: title, content, writer
    private String sortType;     // 정렬 기준: latest, popular
    
    // 생성자 ------------------------------------------------------
    // 기본 생성자
    public BoardSearchCondition() {
	}

    // 조회할 때 사용하는 생성자
    public BoardSearchCondition(String keyword, String type, String sortType) {
    	this.keyword = keyword;
    	this.type = type;
    	this.sortType = sortType;
    }

    // getter, setter ---------------------------------------------
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	// toString ----------------------------------------------------
	@Override
	public String toString() {
		return "BoardSearchCondition [keyword=" + keyword + ", type=" + type + ", sortType=" + sortType + "]";
	}
    
}
