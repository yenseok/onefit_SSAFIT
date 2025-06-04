package com.ssafy.mvc.model.dao;

import java.util.List;

import com.ssafy.mvc.model.dto.Equipment;

public interface EquipmentDao {

    // 1. 태그 포함 운동기구 전체 조회
    List<Equipment> getEquipmentsByGymId(int gymId);  // 이걸로 사용하세요 

    // 2. 키워드 검색 (이것도 태그 포함하도록 아래에서 수정)
    List<Equipment> searchByKeyword(int gymId, String keyword);

    // 3. 단건 조회
    Equipment selectById(int equipmentId);
}
