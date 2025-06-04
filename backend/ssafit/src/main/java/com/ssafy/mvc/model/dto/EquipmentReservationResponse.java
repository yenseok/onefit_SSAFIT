package com.ssafy.mvc.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

//프론트용
public class EquipmentReservationResponse {
    private int reservationId;
    private String equipmentName;
    private String equipmentImg;
    private LocalDate reservationDate;
    private LocalTime reservationTime;

    public EquipmentReservationResponse() {}

    public EquipmentReservationResponse(int reservationId, String equipmentName, String equipmentImg,
                                        LocalDate reservationDate, LocalTime reservationTime) {
        this.reservationId = reservationId;
        this.equipmentName = equipmentName;
        this.equipmentImg = equipmentImg;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentImg() {
        return equipmentImg;
    }

    public void setEquipmentImg(String equipmentImg) {
        this.equipmentImg = equipmentImg;
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
