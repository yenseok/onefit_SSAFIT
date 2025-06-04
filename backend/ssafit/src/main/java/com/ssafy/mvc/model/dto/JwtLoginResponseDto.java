package com.ssafy.mvc.model.dto;

public class JwtLoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private UserResponseDto user;

    public JwtLoginResponseDto() {}

    public JwtLoginResponseDto(String accessToken, String refreshToken, User user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = new UserResponseDto(user); // User  UserResponseDto 변환
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public UserResponseDto getUser() {
        return user;
    }

    public void setUser(UserResponseDto user) {
        this.user = user;
    }
}
