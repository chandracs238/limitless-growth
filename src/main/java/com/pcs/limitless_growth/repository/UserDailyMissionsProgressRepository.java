package com.pcs.limitless_growth.repository;

import com.pcs.limitless_growth.entities.User;
import com.pcs.limitless_growth.entities.UserDailyMissionsProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserDailyMissionsProgressRepository extends JpaRepository<UserDailyMissionsProgress, Long> {
    Optional<UserDailyMissionsProgress> findByUserAndAssignedDate(User user, LocalDate assignedDate);
    Optional<UserDailyMissionsProgress> findTopByUserAndCompletedOrderByDayNumberDesc(User user, boolean completed);
    Optional<UserDailyMissionsProgress> findByUserAndDayNumber(User user, Integer dayNumber);
}
