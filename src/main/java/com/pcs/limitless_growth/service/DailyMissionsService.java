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

    @Transactional
    public UserDailyMissionsProgress completeDailyMissions(Long userId, Integer dayNumber){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        DailyMissions dailyMissions = dailyMissionsRepository.findByDayNumber(dayNumber);

        UserDailyMissionsProgress progress = userDailyMissionsProgressRepository
                .findByUserAndDayNumber(user, dayNumber).orElseThrow(() -> new RuntimeException("Missions not found!"));

        if (progress.isCompleted()){
            throw new RuntimeException("Mission already completed");
        }

        user.setExpPoints(user.getExpPoints() + dailyMissions.getRewardPoints());
        userRepository.save(user);

        progress.setCompleted(true);
        progress.setCompletedAt(LocalDate.now());
        return userDailyMissionsProgressRepository.save(progress);
    }

    @Transactional
    public DailyMissions getTodaysMission(Long userId){
        LocalDate today = LocalDate.now();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Optional<UserDailyMissionsProgress> existingMission =
                userDailyMissionsProgressRepository.findByUserAndAssignedDate(user, today);

        if (existingMission.isPresent()){
            return dailyMissionsRepository.findByDayNumber(existingMission.get().getDayNumber());
        }

        Optional<UserDailyMissionsProgress> lastCompleted =
                userDailyMissionsProgressRepository.findTopByUserAndCompletedOrderByDayNumberDesc(user, true);

        Integer nextDay = lastCompleted.map(UserDailyMissionsProgress::getDayNumber).orElse(0) + 1;

        DailyMissions dailyMissions = dailyMissionsRepository.findByDayNumber(nextDay);

        if (dailyMissions == null){
            throw new RuntimeException("No new missions available");
        }

        UserDailyMissionsProgress progress = new UserDailyMissionsProgress();
        progress.setUser(user);
        progress.setDayNumber(nextDay);
        progress.setCompleted(false);
        progress.setAssignedDate(today);
        userDailyMissionsProgressRepository.save(progress);

        return dailyMissions;
    }

}
