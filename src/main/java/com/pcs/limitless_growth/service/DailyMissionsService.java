package com.pcs.limitless_growth.service;

import com.pcs.limitless_growth.entities.DailyMissions;
import com.pcs.limitless_growth.entities.User;
import com.pcs.limitless_growth.entities.UserDailyMissionsProgress;
import com.pcs.limitless_growth.repository.DailyMissionsRepository;
import com.pcs.limitless_growth.repository.UserDailyMissionsProgressRepository;
import com.pcs.limitless_growth.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DailyMissionsService {

    private final DailyMissionsRepository dailyMissionsRepository;
    private final UserDailyMissionsProgressRepository userDailyMissionsProgressRepository;
    private final UserRepository userRepository;

    public DailyMissionsService(DailyMissionsRepository dailyMissionsRepository, UserDailyMissionsProgressRepository userDailyMissionsProgressRepository, UserRepository userRepository){
        this.dailyMissionsRepository = dailyMissionsRepository;
        this.userDailyMissionsProgressRepository = userDailyMissionsProgressRepository;
        this.userRepository = userRepository;
    }

    public List<DailyMissions> getAllDailyMissions(){
        return dailyMissionsRepository.findAll();
    }

    public DailyMissions getDailyMissionsById(Long id){
        return dailyMissionsRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Quest Not Found"));
    }

    @Transactional
    public String completeDailyMissions(Long userId, Long questId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        DailyMissions dailyMissions = dailyMissionsRepository.findById(questId)
                .orElseThrow(() -> new RuntimeException("Quest not found"));

        Optional<UserDailyMissionsProgress> existingProgress = userDailyMissionsProgressRepository.findByUserAndDailyMissions(user, dailyMissions);
        if (existingProgress.isPresent() && existingProgress.get().isCompleted()){
            return "Quest already completed";
        }

        UserDailyMissionsProgress progress = new UserDailyMissionsProgress();
        progress.setUser(user);
        progress.setDailyMissions(dailyMissions);
        progress.setCompleted(true);
        progress.setCompletedAt(LocalDate.now());

        user.setExpPoints(user.getExpPoints() + dailyMissions.getRewardPoints());

        userDailyMissionsProgressRepository.save(progress);
        userRepository.save(user);

        return "Quest Completed! You earned " + dailyMissions.getRewardPoints() + " points.";
    }
}
