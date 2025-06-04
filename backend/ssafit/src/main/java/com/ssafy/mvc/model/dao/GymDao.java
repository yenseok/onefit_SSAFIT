package com.ssafy.mvc.model.dao;

import java.util.List;
import java.util.Optional;

import com.ssafy.mvc.model.dto.Gym;

public interface GymDao {

	List<Gym> searchGyms(String keyword);
	
	boolean existsGym(int gymId);
	
	List<Gym> selectAll();
	
	Gym selectById(int gymId);

	Optional<Gym> findByName(String name);

}
