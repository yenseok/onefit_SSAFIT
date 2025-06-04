package com.ssafy.mvc.model.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.ssafy.mvc.model.dto.PTReservation;

public interface PTReservationService {

	// pt 예약
	void reserve(PTReservation pt);
	
	// 예약 취소
    void cancel(int ptId);
    
    // 내가 예약한 목록 조회 (해당 날짜에)
    List<PTReservation> getReservationsByUserAndDate(int userId, LocalDate date);
    
    // 해당 날짜, 해당 트레이너가 어느 시간에 이미 예약이 되어있는지 확인
    List<String> getReservedTimes(int trainerId, LocalDate date);
    
    // 이미 예약된 시간인지 확인
    boolean isAlreadyReserved(int trainerId, LocalDate ptDate, LocalTime ptTime);

    int findTrainerIdByName(String trainerName);
}