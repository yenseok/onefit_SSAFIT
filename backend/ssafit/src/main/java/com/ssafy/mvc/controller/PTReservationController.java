package com.ssafy.mvc.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.mvc.model.dto.PTReservation;
import com.ssafy.mvc.model.service.PTReservationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/pt")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = "jwtAuth")
@Tag(name = "PT RESTful API", description = "PT예약 등록/취소/조회")
public class PTReservationController {

    @Autowired
    private PTReservationService service;

    // PT 예약
    @Operation(summary = "PT 예약", description = "PT 예약을 등록합니다.")
    @PostMapping
    public ResponseEntity<String> reserve(@RequestBody PTReservation pt) {
        if (pt.getTrainerName() == null || pt.getTrainerName().isBlank()) {
            return ResponseEntity.badRequest().body("트레이너 이름이 필요합니다.");
        }

        int trainerId = service.findTrainerIdByName(pt.getTrainerName());
        if (trainerId == 0) {
            return ResponseEntity.badRequest().body("해당 이름의 트레이너를 찾을 수 없습니다.");
        }

        pt.setTrainerId(trainerId);

        if (service.isAlreadyReserved(trainerId, pt.getPtDate(), pt.getPtTime())) {
            return ResponseEntity.status(409).body("이미 예약된 시간입니다.");
        }

        service.reserve(pt);
        return ResponseEntity.ok("예약 완료");
    }

    // 예약 취소
    @Operation(summary = "PT 예약 취소", description = "PT 예약을 취소합니다.")
    @DeleteMapping("/{ptId}")
    public ResponseEntity<String> cancel(@PathVariable int ptId) {
        service.cancel(ptId);
        return ResponseEntity.ok("예약 취소 완료");
    }

    // 내 예약 목록 확인
    @Operation(summary = "내 예약 목록 확인", description = "해당 날짜의 내 PT 예약 현황을 확인합니다.")
    @GetMapping("/user/{userId}/date")
    public ResponseEntity<List<PTReservation>> getReservationsByDate(
            @PathVariable int userId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            HttpServletRequest request) {

        Integer tokenUserId = (Integer) request.getAttribute("userId");
        if (!Integer.valueOf(userId).equals(tokenUserId)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(service.getReservationsByUserAndDate(userId, date));
    }

    // 트레이너 예약된 시간 확인 (트레이너 이름 기반으로 조회)
    @Operation(summary = "트레이너 예약 시간 확인", description = "트레이너가 해당 날짜에 이미 예약된 시간을 확인합니다.")
    @GetMapping("/trainer/reserved-times")
    public ResponseEntity<List<String>> getReservedTimes(
            @RequestParam("trainerName") String trainerName,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        int trainerId = service.findTrainerIdByName(trainerName);
        if (trainerId == 0) {
            return ResponseEntity.badRequest().body(List.of()); // 빈 리스트 반환
        }

        return ResponseEntity.ok(service.getReservedTimes(trainerId, date));
    }
}
