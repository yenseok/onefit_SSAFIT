package com.ssafy.mvc.model.dao;

import org.apache.ibatis.annotations.Param;
import com.ssafy.mvc.model.dto.User;

public interface UserDao {

    // 프로필 조회
    User selectUserById(int userId);

    // 프로필 수정
    int updateUser(User user);

    // 비밀번호 변경
    int updatePassword(@Param("userId") int userId, @Param("password") String password);

    // 회원 탈퇴
    int deleteUser(int userId);

    // 회원가입
    int insertUser(User user);

    // 닉네임으로 사용자 조회 (로그인 시 Bcrypt 비교용)
    User selectByNickname(String nickName);

    // user_id로 비밀번호 조회 (verify-password 용)
    String getPasswordByUserId(int userId);

    // 닉네임 중복 검사
    boolean existsByNickname(String nickName);
    

}
