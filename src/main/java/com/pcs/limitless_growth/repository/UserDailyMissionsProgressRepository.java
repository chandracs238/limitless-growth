package com.pcs.limitless_growth.repository;

import com.pcs.limitless_growth.entities.DailyMissions;
import com.pcs.limitless_growth.entities.User;
import com.pcs.limitless_growth.entities.UserDailyMissionsProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDailyMissionsProgressRepository extends JpaRepository<UserDailyMissionsProgress, Long> {
    Optional<UserDailyMissionsProgress> findByUserAndDailyMissions(User user, DailyMissions dailyMissions);
}
