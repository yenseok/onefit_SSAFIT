package com.ssafy.mvc.controller;

import com.ssafy.mvc.model.dto.Equipment;
import com.ssafy.mvc.model.service.EquipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/equipments")
@Tag(name = "Equipment RESTful API", description = "운동기구 조회 및 검색")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    // 1. 헬스장 내 운동기구 전체 조회
    @GetMapping
    @Operation(summary = "운동기구 전체 조회", description = "헬스장 ID로 운동기구 전체를 조회합니다.")
    public ResponseEntity<List<Equipment>> getEquipmentsByGymId(@RequestParam int gymId) {
        List<Equipment> list = equipmentService.getEquipmentsByGymId(gymId);
        return ResponseEntity.ok(list);
    }

    // 2. 운동기구 검색 (이름 or 태그)
    @GetMapping("/search")
    @Operation(summary = "운동기구 검색", description = "기구 이름 또는 태그로 운동기구를 검색합니다.")
    public ResponseEntity<List<Equipment>> searchEquipments(
            @RequestParam int gymId,
            @RequestParam String keyword) {
        List<Equipment> list = equipmentService.searchEquipments(gymId, keyword);
        return ResponseEntity.ok(list);
    }

    // 3. 운동기구 단건 조회
    @GetMapping("/{equipmentId}")
    @Operation(summary = "운동기구 단건 조회", description = "운동기구 ID로 단건 정보를 조회합니다.")
    public ResponseEntity<Equipment> getEquipment(@PathVariable int equipmentId) {
        Equipment equipment = equipmentService.getEquipmentById(equipmentId);
        if (equipment != null) {
            return ResponseEntity.ok(equipment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
