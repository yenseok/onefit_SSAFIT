package com.ssafy.mvc.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.mvc.model.dao.EquipmentDao;
import com.ssafy.mvc.model.dto.Equipment;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private EquipmentDao equipmentDao;

    @Override
    public List<Equipment> getEquipmentsByGymId(int gymId) {
        return equipmentDao.getEquipmentsByGymId(gymId);  // 태그 포함된 메서드로 수정!
    }

    @Override
    public List<Equipment> searchEquipments(int gymId, String keyword) {
        return equipmentDao.searchByKeyword(gymId, keyword);
    }

    @Override
    public Equipment getEquipmentById(int equipmentId) {
        return equipmentDao.selectById(equipmentId);
    }
}
