package com.ssafy.mvc.model.service;

import java.util.List;

import com.ssafy.mvc.model.dto.User;

public interface UserService {

	//프로필 조회
	User getUserProfile(int userId);

	//프로필 수정
	boolean updateUserProfile(User user);

	//비밀번호 변경
	boolean changePassword(int userId, String newPassword);
	
	//회원 탈퇴
	boolean deleteUser(int userId);
	
    // 회원가입
    boolean registerUser(User user);

    // 로그인 
    User login(String nickName, String password);

    // 로그아웃 (세션/토큰 기반이 아니면 내부 처리 없음)
    void logout();

    // 비밀번호 인증
    boolean verifyPassword(int userId, String currentPassword);
    
    
}
