package com.ssafy.mvc.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.mvc.exception.DuplicateNicknameException;
import com.ssafy.mvc.model.dao.GymDao;
import com.ssafy.mvc.model.dao.TrainerDao;
import com.ssafy.mvc.model.dao.UserDao;
import com.ssafy.mvc.model.dto.Gym;
import com.ssafy.mvc.model.dto.Trainer;
import com.ssafy.mvc.model.dto.User;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    
    @Autowired
    private GymDao gymDao;

    @Autowired
    private TrainerDao trainerDao;


    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public User getUserProfile(int userId) {
        User user = userDao.selectUserById(userId);

        if (user != null) {
            if (user.getGymId() > 0) {
                Gym gym = gymDao.selectById(user.getGymId());
                user.setGymName(gym.getGymName());
            }
            if (user.getTrainerId() != null) {
                Trainer trainer = trainerDao.selectById(user.getTrainerId());
                user.setTrainerName(trainer.getTrainerName());
            }
        }

        return user;
    }



    @Override
    public boolean updateUserProfile(User user) {
        return userDao.updateUser(user) > 0;
    }

    @Override
    public boolean changePassword(int userId, String newPassword) {
        String hashed = encoder.encode(newPassword);
        return userDao.updatePassword(userId, hashed) > 0;
    }

    @Override
    public boolean deleteUser(int userId) {
        return userDao.deleteUser(userId) > 0;
    }

    @Override
    public User login(String nickName, String password) {
        User user = userDao.selectByNickname(nickName);
        if (user != null && encoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public void logout() {
        // 클라이언트 측에서 토큰 삭제로 처리
    }

    @Override
    public boolean verifyPassword(int userId, String currentPassword) {
        String storedHash = userDao.getPasswordByUserId(userId);
        return storedHash != null && encoder.matches(currentPassword, storedHash);
    }

    @Override
    public boolean registerUser(User user) {
        if (userDao.existsByNickname(user.getNickName())) {
            throw new DuplicateNicknameException("이미 사용 중인 닉네임입니다.");
        }

        // 비밀번호 암호화
        user.setPassword(encoder.encode(user.getPassword()));

        user.setRole("USER");
        
        return userDao.insertUser(user) == 1;
    }


}
