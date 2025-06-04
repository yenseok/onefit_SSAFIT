package com.ssafy.mvc.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.mvc.model.dto.Trainer;
import com.ssafy.mvc.model.dto.SearchCondition;


public interface TrainerService {

    //  트레이너 키워드 검색
    List<Trainer> searchTrainers(String keyword);

    //  트레이너 ID 존재 여부 확인
    boolean existsTrainer(int trainerId);

 // 트레이너 단건 조회
    Trainer getTrainerById(int trainerId);

    List<Trainer> getAllTrainers();
    
    List<Trainer> searchTrainersByGym(String keyword, int gymId);
    
    Trainer findById(int trainerId);
}
