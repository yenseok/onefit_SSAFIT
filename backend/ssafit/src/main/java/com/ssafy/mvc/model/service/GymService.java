package com.ssafy.mvc.model.service;

import java.util.List;

import com.ssafy.mvc.model.dto.Gym;

public interface GymService {
    //  헬스장 키워드 검색
    List<Gym> searchGyms(String keyword);

    //  헬스장 ID 존재 여부 확인
    boolean existsGym(int gymId);
    
    List<Gym> getAllGyms();

    boolean existsByName(String name);
}
