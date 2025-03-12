package com.pcs.limitless_growth.controller;

import com.pcs.limitless_growth.dto.DailyMissionsProgressResponse;
import com.pcs.limitless_growth.entities.DailyMissions;
import com.pcs.limitless_growth.service.DailyMissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/dailyMissions")
public class DailyMissionsController {

    @Autowired
    private DailyMissionsService dailyMissionsService;


    @GetMapping
    public ResponseEntity<DailyMissions> getDailyMissions(@PathVariable Long userId){
        return ResponseEntity.ok(dailyMissionsService.getTodaysMission(userId));
    }

    @PostMapping("/complete/{dayNumber}")
    public ResponseEntity<DailyMissionsProgressResponse> completeMission(@PathVariable Long userId, @PathVariable Integer dayNumber){
        return ResponseEntity.ok(dailyMissionsService.completeDailyMissions(userId, dayNumber));
    }

}
