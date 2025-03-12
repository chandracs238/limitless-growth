package com.pcs.limitless_growth.service;

import com.pcs.limitless_growth.dto.DailyMissionsProgressResponse;
import com.pcs.limitless_growth.entities.DailyMissions;
import com.pcs.limitless_growth.entities.User;
import com.pcs.limitless_growth.entities.UserDailyMissionsProgress;
import com.pcs.limitless_growth.exception.NoMissionsAvailableException;
import com.pcs.limitless_growth.repository.DailyMissionsRepository;
import com.pcs.limitless_growth.repository.UserDailyMissionsProgressRepository;
import com.pcs.limitless_growth.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DailyMissionsService {

    private final DailyMissionsRepository dailyMissionsRepository;
    private final UserDailyMissionsProgressRepository userDailyMissionsProgressRepository;
    private final UserRepository userRepository;

    public DailyMissionsService(DailyMissionsRepository dailyMissionsRepository, UserDailyMissionsProgressRepository userDailyMissionsProgressRepository, UserRepository userRepository) {
        this.dailyMissionsRepository = dailyMissionsRepository;
        this.userDailyMissionsProgressRepository = userDailyMissionsProgressRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public DailyMissionsProgressResponse completeDailyMissions(Long userId, Integer dayNumber) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        DailyMissions dailyMissions = dailyMissionsRepository.findByDayNumber(dayNumber);

        UserDailyMissionsProgress progress = userDailyMissionsProgressRepository
                .findByUserAndDayNumber(user, dayNumber).orElseThrow(() -> new NoMissionsAvailableException("Mission not found"));

        if (progress.isCompleted()) {
            throw new RuntimeException("Mission already completed");
        }

        user.setExpPoints(user.getExpPoints() + dailyMissions.getRewardPoints());
        userRepository.save(user);

        progress.setCompleted(true);
        progress.setCompletedAt(LocalDate.now());
        userDailyMissionsProgressRepository.save(progress);

        return new DailyMissionsProgressResponse(progress);
    }

    @Transactional
    public DailyMissions getTodaysMission(Long userId) {
        LocalDate today = LocalDate.now();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if today's mission is already assigned
        return userDailyMissionsProgressRepository.findByUserAndAssignedDate(user, today)
                .map(progress -> dailyMissionsRepository.findByDayNumber(progress.getDayNumber()))
                .orElseGet(() -> assignNewMission(user, today));
    }

    private DailyMissions assignNewMission(User user, LocalDate today) {
        // Get the last completed mission and determine the next day's mission
        Integer nextDay = userDailyMissionsProgressRepository.findTopByUserAndCompletedOrderByDayNumberDesc(user, true)
                .map(UserDailyMissionsProgress::getDayNumber)
                .orElse(0) + 1;

        DailyMissions dailyMissions = dailyMissionsRepository.findByDayNumber(nextDay);
        if (dailyMissions == null) {
            throw new NoMissionsAvailableException("No new missions available for day " + nextDay);
        }

        // Save the new mission assignment
        userDailyMissionsProgressRepository.save(new UserDailyMissionsProgress(null, user, nextDay, false, today, null));

        return dailyMissions;
    }
}
