package com.ssafy.mvc.controller;

import com.ssafy.mvc.model.dto.EquipmentReservationRequest;
import com.ssafy.mvc.model.dto.EquipmentReservationResponse;
import com.ssafy.mvc.model.service.EquipmentReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/reservations")
@PreAuthorize("isAuthenticated()") //  로그인된 사용자만 접근 허용
@SecurityRequirement(name = "jwtAuth") //  Swagger 토큰 필요 표시
@Tag(name = "Equipment Reservation API", description = "운동기구 예약 관련 기능")
public class EquipmentReservationController {

    @Autowired
    private EquipmentReservationService reservationService;

    // 1. 예약 등록
    @PostMapping
    public ResponseEntity<String> reserve(@RequestBody EquipmentReservationRequest dto, HttpServletRequest request) {
        Integer tokenUserId = (Integer) request.getAttribute("userId");
        if (tokenUserId == null) {
            return ResponseEntity.status(403).body("인증 정보 없음");
        }

        dto.setUserId(tokenUserId); // 강제 설정
        boolean result = reservationService.reserveEquipment(dto);
        return result ?
            ResponseEntity.ok("예약 성공") :
            ResponseEntity.badRequest().body("예약 실패");
    }


    // 2. 예약된 시간 조회 (비로그인도 가능하게 두고 싶다면 @PermitAll 사용)
    @GetMapping("/{equipmentId}/times")
    @Operation(summary = "이미 예약된 시간 조회", description = "특정 날짜에 이미 예약된 시간을 반환합니다.")
    public ResponseEntity<List<java.time.LocalTime>> getReservedTimes(
            @PathVariable int equipmentId,
            @RequestParam LocalDate date) {
        List<java.time.LocalTime> times = reservationService.getReservedTimes(equipmentId, date);
        return ResponseEntity.ok(times);
    }

    // 3. 사용자 예약 목록 조회 (요청자 본인인지 확인하고 싶다면 HttpServletRequest 활용)
    @GetMapping("/user/{userId}")
    @Operation(summary = "운동기구 예약 목록 조회", description = "사용자의 예약된 운동기구 목록을 조회합니다.")
    public ResponseEntity<List<EquipmentReservationResponse>> getReservations(
            @PathVariable int userId,
            HttpServletRequest request) {

        Integer tokenUserId = (Integer) request.getAttribute("userId");
        if (tokenUserId == null || tokenUserId != userId) {
            return ResponseEntity.status(403).body(null);
        }

        return ResponseEntity.ok(reservationService.getUserReservations(userId));
    }

    // 4. 예약 취소
    @DeleteMapping("/{reservationId}")
    @Operation(summary = "운동기구 예약 취소", description = "예약 ID로 운동기구 예약을 취소합니다.")
    public ResponseEntity<String> cancel(@PathVariable int reservationId) {
        boolean result = reservationService.cancelReservation(reservationId);
        return result ?
                ResponseEntity.ok("예약 취소 성공") :
                ResponseEntity.badRequest().body("예약 취소 실패");
    }
}
