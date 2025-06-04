package com.ssafy.mvc.model.dto;

import java.time.LocalTime;

// PT + 운동기구 예약 통합 DTO
public class UserDailyReservation {

	private String type; // "PT" 또는 "EQUIPMENT"
	private String name; // 트레이너 이름 또는 운동기구 이름
	private String imageUrl; // 트레이너 사진 또는 운동기구 이미지
	private LocalTime time; // 예약 시간
	private int reservationId;

	public UserDailyReservation() {
	}

	public UserDailyReservation(String type, String name, String imageUrl, LocalTime time, int reservationId) {
		super();
		this.type = type;
		this.name = name;
		this.imageUrl = imageUrl;
		this.time = time;
		this.reservationId = reservationId;
	}

	// Getter/Setter
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public int getReservationId() {
		return reservationId;
	}

	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}
}
