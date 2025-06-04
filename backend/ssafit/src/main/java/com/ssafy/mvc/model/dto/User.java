package com.ssafy.mvc.model.dto;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User implements UserDetails{
	private int userId;
	private String name;
	private String nickName;
	private String email;
	private String password;
	private String phone;
	private int gymId;
	private Integer trainerId; // nullable
	private String profileImage;
	private String role;
	private String gymName;
	private String trainerName;

	// Getter & Setter
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
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
	public String getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	public String getRole() {
	    return role;
	}
	public void setRole(String role) {
	    this.role = role;
	}
	public String getGymName() {
	    return gymName;
	}
	public void setGymName(String gymName) {
	    this.gymName = gymName;
	}
	public String getTrainerName() {
	    return trainerName;
	}
	public void setTrainerName(String trainerName) {
	    this.trainerName = trainerName;
	}


	@Override
	public String toString() {
	    return "User [userId=" + userId + ", name=" + name + ", email=" + email +
	           ", password=" + password + ", phone=" + phone + ", gymId=" + gymId +
	           ", trainerId=" + trainerId + ", profileImage=" + profileImage +
	           ", role=" + role + "]";
	}

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_" + role); // 예: ROLE_USER
    }

    @Override
    public String getUsername() {
        return nickName; // 인증 시 사용되는 ID
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}





