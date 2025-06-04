package com.ssafy.mvc.model.service;

import java.util.List;

import com.ssafy.mvc.model.dto.Equipment;

public interface EquipmentService {

    // 헬스장 내 운동기구 전체 조회
    List<Equipment> getEquipmentsByGymId(int gymId);

    // 운동기구 이름 또는 태그 기반 검색
    List<Equipment> searchEquipments(int gymId, String keyword);

    // 특정 운동기구 단건 조회
    Equipment getEquipmentById(int equipmentId);
}
