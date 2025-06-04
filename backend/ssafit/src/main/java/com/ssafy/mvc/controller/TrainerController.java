package com.ssafy.mvc.controller;

import com.ssafy.mvc.model.dto.Trainer;
import com.ssafy.mvc.model.service.TrainerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/trainers")
@Tag(name = "Trainer RESTful API", description = "트레이너 검색 API")
public class TrainerController {

    @Autowired
    private TrainerService trainerService;

    //  트레이너 이름 키워드 검색
    @GetMapping
    @Operation(summary = "트레이너 검색", description = "트레이너 이름 키워드로 검색합니다.")
    public ResponseEntity<List<Trainer>> searchTrainers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer gymId) {

        List<Trainer> trainers;

        if (gymId != null) {
            trainers = trainerService.searchTrainersByGym(keyword, gymId);
        } else {
            trainers = keyword == null || keyword.isBlank()
                ? trainerService.getAllTrainers()
                : trainerService.searchTrainers(keyword);
        }

        return ResponseEntity.ok(trainers);
    }


}
