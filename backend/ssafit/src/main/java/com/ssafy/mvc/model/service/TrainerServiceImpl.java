package com.ssafy.mvc.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.mvc.model.dao.TrainerDao;
import com.ssafy.mvc.model.dto.Trainer;

@Service
public class TrainerServiceImpl implements TrainerService {

    @Autowired
    private TrainerDao trainerDao;

    @Override
    public List<Trainer> searchTrainers(String keyword) {
        return trainerDao.searchTrainers(keyword);
    }

    @Override
    public boolean existsTrainer(int trainerId) {
        return trainerDao.existsTrainer(trainerId);
    }

    @Override
    public Trainer getTrainerById(int trainerId) {
        return trainerDao.selectById(trainerId); // DAO에 정의되어 있어야 함
    }
    
    @Override
    public List<Trainer> getAllTrainers() {
        return trainerDao.selectAll();
    }
    
    @Override
    public List<Trainer> searchTrainersByGym(String keyword, int gymId) {
        return trainerDao.searchTrainersByGym("%" + keyword + "%", gymId);
    }
    
    @Override
    public Trainer findById(int trainerId) {
        return trainerDao.selectById(trainerId);
    }

}
