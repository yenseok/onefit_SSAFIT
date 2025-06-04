package com.ssafy.mvc.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class PTReservation {

	// 멤버변수 ----------------------------------------------------
	private int ptId;          // pt 예약 id
    private int userId;        // 회원 id
    private int trainerId;
	private String trainerName;
    private LocalDate ptDate;  // pt 예약 날짜
    private LocalTime ptTime;  // pt 예약 시간

    // 생성자 ------------------------------------------------------
    // 기본 생성자
    public PTReservation() {}

    // 예약 등록할 때 필요...?
    public PTReservation(int ptId, int userId, int trainerId, LocalDate ptDate, LocalTime ptTime) {
        this.ptId = ptId;
        this.userId = userId;
        this.trainerId = trainerId;
        this.ptDate = ptDate;
        this.ptTime = ptTime;
    }

    // getter, setter ---------------------------------------------
	public int getPtId() {
		return ptId;
	}

	public void setPtId(int ptId) {
		this.ptId = ptId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTrainerId() {
		return trainerId;
	}

	public void setTrainerId(int trainerId) {
		this.trainerId = trainerId;
	}

	public LocalDate getPtDate() {
		return ptDate;
	}

	public void setPtDate(LocalDate ptDate) {
		this.ptDate = ptDate;
	}

	public LocalTime getPtTime() {
		return ptTime;
	}

	public void setPtTime(LocalTime ptTime) {
		this.ptTime = ptTime;
	}
	
	

	public String getTrainerName() {
		return trainerName;
	}

	public void setTrainerName(String trainerName) {
		this.trainerName = trainerName;
	}

	// toString ----------------------------------------------------
	@Override
	public String toString() {
		return "PTReservation [ptId=" + ptId + ", userId=" + userId + ", trainerId=" + trainerId + ", ptDate=" + ptDate
				+ ", ptTime=" + ptTime + "]";
	}
    
}
