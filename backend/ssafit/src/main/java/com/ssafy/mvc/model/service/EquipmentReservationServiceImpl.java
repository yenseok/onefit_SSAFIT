package com.ssafy.mvc.model.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.mvc.model.dao.EquipmentReservationDao;
import com.ssafy.mvc.model.dto.EquipmentReservation;
import com.ssafy.mvc.model.dto.EquipmentReservationRequest;
import com.ssafy.mvc.model.dto.EquipmentReservationResponse;

@Service
public class EquipmentReservationServiceImpl implements EquipmentReservationService {

    @Autowired
    private EquipmentReservationDao reservationDao;

    @Override
    public boolean reserveEquipment(EquipmentReservationRequest dto) {
        return reservationDao.insertReservation(dto) > 0;
    }

    @Override
    public List<LocalTime> getReservedTimes(int equipmentId, LocalDate reservationDate) {
        return reservationDao.selectReservedTimes(equipmentId, reservationDate);
    }

    @Override
    public List<EquipmentReservationResponse> getUserReservations(int userId) {
        return reservationDao.selectReservationsByUserId(userId);
    }

    @Override
    public boolean cancelReservation(int reservationId) {
        return reservationDao.deleteReservation(reservationId) > 0;
    }
    
    @Override
    public List<EquipmentReservation> getReservationsByDate(int userId, LocalDate date) {
        return reservationDao.selectByUserAndDate(userId, date);
    }

}
