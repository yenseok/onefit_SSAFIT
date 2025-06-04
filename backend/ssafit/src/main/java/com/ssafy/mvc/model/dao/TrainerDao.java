package com.ssafy.mvc.model.dao;

import java.util.List;
import java.util.Map;

import com.ssafy.mvc.model.dto.SearchCondition;
import com.ssafy.mvc.model.dto.Trainer;
import com.ssafy.mvc.model.dto.User;

public interface TrainerDao {
    // 트레이너 키워드로 검색
    List<Trainer> searchTrainers(String keyword);

    // 트레이너 ID 존재 여부 확인
    boolean existsTrainer(int trainerId);

	Trainer selectById(int trainerId);
	
	List<Trainer> selectAll();
	
	List<Trainer> searchTrainersByGym(String keyword, int gymId);
	
	Trainer selectTrainerById(int trainerId); //  지금 인터페이스에는 없음

}