package com.ssafy.mvc.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class EquipmentReservationRequest {
    private int userId;
    private int equipmentId;
    private LocalDate reservationDate;
    private LocalTime reservationTime;

    public EquipmentReservationRequest() {}

    public EquipmentReservationRequest(int userId, int equipmentId, LocalDate reservationDate, LocalTime reservationTime) {
        this.userId = userId;
        this.equipmentId = equipmentId;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalTime reservationTime) {
        this.reservationTime = reservationTime;
    }
}
