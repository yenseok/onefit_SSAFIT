package com.ssafy.mvc.controller;

import com.ssafy.mvc.model.dto.Gym;
import com.ssafy.mvc.model.service.GymService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/gyms")
@Tag(name = "Gym RESTful API", description = "헬스장 검색 API")
public class GymController {

    @Autowired
    private GymService gymService;

    // 키워드로 헬스장 검색
    @GetMapping
    @Operation(summary = "헬스장 검색", description = "이름 키워드로 헬스장을 검색합니다.")
    public ResponseEntity<List<Gym>> searchGyms(@RequestParam(required = false) String keyword) {
        keyword = (keyword != null) ? keyword.trim() : null;

        List<Gym> gyms = (keyword == null || keyword.isEmpty())
            ? gymService.getAllGyms()
            : gymService.searchGyms(keyword);

        return ResponseEntity.ok(gyms);
    }


}
