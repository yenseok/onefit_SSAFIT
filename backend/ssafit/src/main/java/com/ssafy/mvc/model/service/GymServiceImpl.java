package com.ssafy.mvc.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.mvc.model.dao.GymDao;
import com.ssafy.mvc.model.dto.Gym;

@Service
public class GymServiceImpl implements GymService {

    @Autowired
    private GymDao gymDao;

    @Override
    public List<Gym> searchGyms(String keyword) {
        return gymDao.searchGyms(keyword);
    }

    @Override
    public boolean existsGym(int gymId) {
        return gymDao.existsGym(gymId);
    }
    
    @Override
    public List<Gym> getAllGyms() {
        return gymDao.selectAll();
    }
    
    @Override
    public boolean existsByName(String name) {
        return gymDao.findByName(name).isPresent();
    }

}
