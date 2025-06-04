package com.ssafy.mvc.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EquipmentReservation {
    private int reservationId;
    private int userId;
    private int equipmentId;
    @JsonProperty("reservation_date")
    private LocalDate reservationDate;
    @JsonProperty("reservation_time")
    private LocalTime reservationTime;

    public EquipmentReservation() {}

    public EquipmentReservation(int reservationId, int userId, int equipmentId, LocalDate reservationDate, LocalTime reservationTime) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.equipmentId = equipmentId;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
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
