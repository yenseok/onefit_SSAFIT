package com.ssafy.mvc.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.mvc.model.dto.PasswordChangeDto;
import com.ssafy.mvc.model.dto.Trainer;
import com.ssafy.mvc.model.dto.User;
import com.ssafy.mvc.model.service.GymService;
import com.ssafy.mvc.model.service.TrainerService;
import com.ssafy.mvc.model.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@RequestMapping("/users")
@Tag(name = "User RESTful API", description = "회원 조회, 수정, 비밀번호 변경")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GymService gymService;
    
    @Autowired
    private TrainerService trainerService;

    // 1. 프로필 조회
    @GetMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal.userId")
    @Operation(summary = "프로필 조회", description = "로그인 사용자의 프로필 정보를 반환합니다.", security = @SecurityRequirement(name = "jwtAuth"))
    public ResponseEntity<?> getUser(@PathVariable("userId") int userId) {
        User user = userService.getUserProfile(userId);
        return (user != null) ? ResponseEntity.ok(user)
                              : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // 2. 프로필 수정
    @PutMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("#userId == principal.userId")
    @Operation(summary = "프로필 수정", description = "회원 정보와 프로필 이미지를 함께 수정합니다.", security = @SecurityRequirement(name = "jwtAuth"))
    public ResponseEntity<String> updateUser(
            @PathVariable("userId") int userId,
            @RequestPart("user") User user,
            @RequestPart(value = "file", required = false) MultipartFile file,
            Authentication authentication) {

        user.setUserId(userId);
        
        if (!gymService.existsByName(user.getGymName())) {
            return ResponseEntity.badRequest().body("존재하지 않는 헬스장입니다.");
        }

        
        if (user.getTrainerName() != null && !user.getTrainerName().isBlank()) {
            // trainerId가 null이면 유효하지 않음
            if (user.getTrainerId() == null) {
                return ResponseEntity.badRequest().body("트레이너가 선택되지 않았습니다.");
            }

            // 실제 DB에서 trainerId로 trainer 조회
            Trainer trainer = trainerService.findById(user.getTrainerId());
            if (trainer == null || !trainer.getTrainerName().equals(user.getTrainerName())) {
                return ResponseEntity.badRequest().body("트레이너 정보가 일치하지 않습니다.");
            }

            // 추가: gymId가 있는 경우 trainer의 gymId와 일치하는지 확인
            if (user.getGymId() != 0 && trainer.getGymId() != user.getGymId()) {
                return ResponseEntity.badRequest().body(
                    String.format("\"%s\" 트레이너는 선택된 헬스장에 소속되어 있지 않습니다.", user.getTrainerName())
                );
            }
        }


        if (file != null && !file.isEmpty()) {
            String originalName = file.getOriginalFilename();
            if (!(originalName.endsWith(".jpg") || originalName.endsWith(".png") ||
                  originalName.endsWith(".jpeg") || originalName.endsWith(".JPG") || originalName.endsWith(".PNG"))) {
                return ResponseEntity.badRequest().body("이미지 형식은 .jpg 또는 .png만 허용됩니다.");
            }

            String fileName = UUID.randomUUID() + "_" + originalName;
            try {
                File saveDir = new ClassPathResource("static/upload/profile").getFile();
                if (!saveDir.exists()) saveDir.mkdirs();

                File saveFile = new File(saveDir, fileName);
                file.transferTo(saveFile);
                user.setProfileImage("/upload/profile/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 저장 실패");
            }
        }
        
        System.out.println("== principal: " + authentication.getPrincipal());
        System.out.println("== principal class: " + authentication.getPrincipal().getClass().getName());
        
        boolean result = userService.updateUserProfile(user);
        return result ? ResponseEntity.ok("회원 정보 수정 완료")
                      : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원 정보 수정 실패");
    }

    // 3. 비밀번호 변경
    @PutMapping("/{userId}/password")
    @PreAuthorize("#userId == principal.userId") //  principal의 userId 필드와 비교
    @Operation(summary = "비밀번호 변경", description = "새로운 비밀번호로 사용자의 비밀번호를 변경합니다.", security = @SecurityRequirement(name = "jwtAuth"))
    public ResponseEntity<String> changePassword(
            @PathVariable int userId,
            @RequestBody PasswordChangeDto dto) {

        boolean result = userService.changePassword(userId, dto.getNewPassword());
        return result ? ResponseEntity.ok("비밀번호 변경 완료")
                      : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호 변경 실패");
    }


    // 4. 회원 탈퇴
    @DeleteMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal")
    @Operation(summary = "회원 탈퇴", description = "회원 ID를 이용해 해당 회원을 탈퇴 처리합니다.", security = @SecurityRequirement(name = "jwtAuth"))
    public ResponseEntity<String> deleteUser(@PathVariable("userId") int userId) {
        boolean result = userService.deleteUser(userId);
        return result ? ResponseEntity.ok("회원 탈퇴 완료")
                      : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원 탈퇴 실패");
    }
}