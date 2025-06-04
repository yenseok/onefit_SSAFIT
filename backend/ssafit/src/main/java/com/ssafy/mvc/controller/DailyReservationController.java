package com.ssafy.mvc.controller;

import com.ssafy.mvc.model.dto.*;
import com.ssafy.mvc.model.service.EquipmentReservationService;
import com.ssafy.mvc.model.service.EquipmentService;
import com.ssafy.mvc.model.service.PTReservationService;
import com.ssafy.mvc.model.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/daily-reservations")
@Tag(name = "통합 예약 조회 API", description = "하루의 PT 및 운동기구 예약을 시간순으로 조회")
public class DailyReservationController {

    @Autowired
    private PTReservationService ptReservationService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private EquipmentReservationService equipmentReservationService;

    @Autowired
    private EquipmentService equipmentService;

    @GetMapping("/user/{userId}")
    @Operation(summary = "특정 날짜의 예약 통합 조회", description = "사용자의 PT와 운동기구 예약을 시간순으로 조회합니다.")
    public ResponseEntity<List<UserDailyReservation>> getDailyReservations(
            @PathVariable int userId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<UserDailyReservation> results = new ArrayList<>();

        // 1. PT 예약 조회
        List<PTReservation> ptList = ptReservationService.getReservationsByUserAndDate(userId, date);
        for (PTReservation pt : ptList) {
            Trainer trainer = trainerService.getTrainerById(pt.getTrainerId());
            results.add(new UserDailyReservation(
            	    "PT",
            	    trainer.getTrainerName(),
            	    trainer.getTrainerImg(),
            	    pt.getPtTime(),
            	    pt.getPtId() // ✅ 기존 pt.getReservationId() → pt.getPtId()
            	));



        }

        // 2. 운동기구 예약 조회
        List<EquipmentReservation> eqList = equipmentReservationService.getReservationsByDate(userId, date);
        for (EquipmentReservation eq : eqList) {
            Equipment equipment = equipmentService.getEquipmentById(eq.getEquipmentId());
            results.add(new UserDailyReservation(
            	    "EQUIPMENT",
            	    equipment.getEquipmentName(),
            	    equipment.getEquipmentImg(),
            	    eq.getReservationTime(),
            	    eq.getReservationId()   //  reservationId는 마지막에 넣어야 함
            	));


        }

        // 3. 시간순 정렬
        results.sort(Comparator.comparing(UserDailyReservation::getTime));

        return ResponseEntity.ok(results);
    }
}
