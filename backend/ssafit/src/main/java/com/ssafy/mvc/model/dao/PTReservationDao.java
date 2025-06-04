package com.ssafy.mvc.model.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ssafy.mvc.model.dto.PTReservation;

public interface PTReservationDao {

	// pt 예약
	void insert(PTReservation pt);
	
	// 예약 취소
    void delete(int ptId);
    
    // 내가 예약한 목록 조회(해당 날짜에)
    List<PTReservation> selectByUserIdAndDate(@Param("userId") int userId, @Param("ptDate") LocalDate date);
    
    // 해당 날짜, 해당 트레이너가 어느 시간에 이미 예약이 되어있는지 확인
    List<String> selectReservedTimes(@Param("trainerId") int trainerId, @Param("ptDate") LocalDate date);

    // 이미 예약된 시간인지 확인
    int countByTrainerAndDateTime(int trainerId, LocalDate ptDate, LocalTime ptTime);

    // 트레이너 이름으로 트레이너 ID 찾기
    int findTrainerIdByName(@Param("trainerName") String trainerName);
}