package com.pcs.limitless_growth.repository;

import com.pcs.limitless_growth.entities.DailyMissions;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DailyMissionsRepository extends JpaRepository<DailyMissions, Long> {
    DailyMissions findByDayNumber(Integer dayNumber);
}
