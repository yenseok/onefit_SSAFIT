package com.ssafy.mvc.model.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.ssafy.mvc.model.dto.EquipmentReservation;
import com.ssafy.mvc.model.dto.EquipmentReservationRequest;
import com.ssafy.mvc.model.dto.EquipmentReservationResponse;

public interface EquipmentReservationService {

    // 1. 예약 등록
    boolean reserveEquipment(EquipmentReservationRequest dto);

    // 2. 이미 예약된 시간 조회
    List<LocalTime> getReservedTimes(int equipmentId, LocalDate reservationDate);
    
 // 특정 날짜에 사용자의 운동기구 예약 전체 조회
    List<EquipmentReservation> getReservationsByDate(int userId, LocalDate date);


    // 3. 예약 목록 조회
    List<EquipmentReservationResponse> getUserReservations(int userId);

    // 4. 예약 취소
    boolean cancelReservation(int reservationId);
}
