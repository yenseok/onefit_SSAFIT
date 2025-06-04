package com.ssafy.mvc.controller;

import com.ssafy.mvc.exception.DuplicateNicknameException;
import com.ssafy.mvc.model.dto.*;
import com.ssafy.mvc.model.service.UserService;
import com.ssafy.mvc.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
@Tag(name = "Auth RESTful API", description = "회원가입, 로그인, 로그아웃, 비밀번호 인증")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "헬스장 입력은 필수입니다.")
    public ResponseEntity<String> signup(@RequestBody User user) {
        if (user.getGymId() == 0) {
            return ResponseEntity.badRequest().body("헬스장은 필수 선택입니다.");
        }

        try {
            userService.registerUser(user); // 내부에서 BCrypt 암호화 필요
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
        } catch (DuplicateNicknameException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "닉네임과 비밀번호로 로그인하고 JWT 토큰을 반환합니다.")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        User user = userService.login(loginDto.getNickName(), loginDto.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("닉네임 또는 비밀번호가 올바르지 않습니다.");
        }

        //  role 포함하여 JWT 생성
        String accessToken = JwtUtil.generateAccessToken(user.getUserId(), user.getNickName(), user.getRole());
        String refreshToken = JwtUtil.generateRefreshToken(user.getUserId(), user.getNickName(), user.getGymId());

        // 비밀번호 제외한 DTO 생성
        UserResponseDto userDto = new UserResponseDto(user);

        JwtLoginResponseDto response = new JwtLoginResponseDto(accessToken, refreshToken, user);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "프론트에서 토큰 삭제 등 로그아웃 처리")
    public ResponseEntity<String> logout() {
        userService.logout(); // 세션/토큰 저장 안 하면 생략 가능
        return ResponseEntity.ok("로그아웃 완료");
    }

    @PostMapping("/verify-password")
    @Operation(summary = "비밀번호 인증", description = "입력한 현재 비밀번호가 DB와 일치하는지 검증합니다.")
    public ResponseEntity<String> verifyPassword(@RequestBody PasswordVerifyDto dto) {
        boolean result = userService.verifyPassword(dto.getUserId(), dto.getCurrentPassword());
        return result ?
                ResponseEntity.ok("비밀번호 일치") :
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호 불일치");
    }
}
