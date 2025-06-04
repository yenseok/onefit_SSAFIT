package com.ssafy.mvc.model.dto;

//클라이언트 응답용
public class UserResponseDto {
    private int userId;
    private String name;
    private String nickName;
    private String email;
    private String phone;
    private int gymId;
    private Integer trainerId;
    private String profileImage;

    public UserResponseDto(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.nickName = user.getNickName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.gymId = user.getGymId();
        this.trainerId = user.getTrainerId();
        this.profileImage = user.getProfileImage();
    }

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getGymId() {
		return gymId;
	}

	public void setGymId(int gymId) {
		this.gymId = gymId;
	}

	public Integer getTrainerId() {
		return trainerId;
	}

	public void setTrainerId(Integer trainerId) {
		this.trainerId = trainerId;
	}
	
	

    
}
