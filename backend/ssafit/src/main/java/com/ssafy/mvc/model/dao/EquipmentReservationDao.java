package com.ssafy.mvc.model.dao;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalTime;

import com.ssafy.mvc.model.dto.EquipmentReservation;
import com.ssafy.mvc.model.dto.EquipmentReservationRequest;
import com.ssafy.mvc.model.dto.EquipmentReservationResponse;

public interface EquipmentReservationDao {

    // 1. 운동기구 예약 등록
    int insertReservation(EquipmentReservationRequest dto);

    // 2. 특정 날짜에 이미 예약된 시간 조회
    List<LocalTime> selectReservedTimes(int equipmentId, LocalDate reservationDate);

    // 3. 특정 유저의 운동기구 예약 목록 조회
    List<EquipmentReservationResponse> selectReservationsByUserId(int userId);

 // 특정 날짜의 예약 전체 조회
    List<EquipmentReservation> selectByUserAndDate(int userId, LocalDate date);
    
    // 4. 예약 취소
    int deleteReservation(int reservationId);
}
