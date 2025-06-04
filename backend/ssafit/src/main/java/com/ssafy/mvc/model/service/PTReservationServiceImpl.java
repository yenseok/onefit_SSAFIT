package com.ssafy.mvc.model.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.mvc.model.dao.PTReservationDao;
import com.ssafy.mvc.model.dto.PTReservation;

@Service
public class PTReservationServiceImpl implements PTReservationService{

	@Autowired
    private PTReservationDao dao;

    // 트레이너 이름으로 ID 조회
    private int resolveTrainerId(String trainerName) {
        if (trainerName == null || trainerName.isBlank()) {
            return 0;
        }
        return dao.findTrainerIdByName(trainerName);
    }

    // pt 예약
    @Override
    public void reserve(PTReservation pt) {
        // trainerId가 없다면 trainerName으로 조회
        if (pt.getTrainerId() == 0 && pt.getTrainerName() != null) {
            int trainerId = resolveTrainerId(pt.getTrainerName());
            pt.setTrainerId(trainerId);
        }

        dao.insert(pt);
    }

    // 예약 취소
    @Override
    public void cancel(int ptId) {
        dao.delete(ptId);
    }

    // 내가 예약한 목록 조회 (해당 날짜에)
    @Override
    public List<PTReservation> getReservationsByUserAndDate(int userId, LocalDate date) {
        return dao.selectByUserIdAndDate(userId, date);
    }

    // 트레이너 이름 기반 예약된 시간 확인
    @Override
    public List<String> getReservedTimes(int trainerId, LocalDate date) {
        if (trainerId == 0) {
            return List.of(); // 빈 리스트 반환
        }
        return dao.selectReservedTimes(trainerId, date);
    }

    // 트레이너 이름 기반 예약 여부 확인
    @Override
    public boolean isAlreadyReserved(int trainerId, LocalDate ptDate, LocalTime ptTime) {
        if (trainerId == 0) {
            return false;
        }
        return dao.countByTrainerAndDateTime(trainerId, ptDate, ptTime) > 0;
    }

    // 트레이너 이름 기반 ID 조회
    @Override
    public int findTrainerIdByName(String trainerName) {
        return resolveTrainerId(trainerName);
    }

}