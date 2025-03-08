package com.pcs.limitless_growth.controller;

import com.pcs.limitless_growth.entities.DailyMissions;
import com.pcs.limitless_growth.service.DailyMissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DailyMissionsController {

    @Autowired
    private DailyMissionsService dailyMissionsService;

    @GetMapping("/dailyMissions")
    public List<DailyMissions> getAllDailyMissions(){
        return dailyMissionsService.getAllDailyMissions();
    }

    @GetMapping("/dailyMissions/{id}")
    public DailyMissions getDailyMissionsById(@PathVariable Long id){
        return dailyMissionsService.getDailyMissionsById(id);
    }

    @PostMapping("/dailyMissions/{dailyMissionsId}/complete/{userId}")
    public ResponseEntity<String> completeQuest(@PathVariable Long userId, @PathVariable Long dailyMissionsId){
        String result = dailyMissionsService.completeDailyMissions(userId, dailyMissionsId);
        return ResponseEntity.ok(result);
    }
}
