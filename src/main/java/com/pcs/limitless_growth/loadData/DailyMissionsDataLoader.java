package com.pcs.limitless_growth.loadData;

import com.pcs.limitless_growth.entities.DailyMissions;
import com.pcs.limitless_growth.entities.Tier;
import com.pcs.limitless_growth.repository.DailyMissionsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DailyMissionsDataLoader implements CommandLineRunner {

    private final DailyMissionsRepository dailyMissionsRepository;

    public DailyMissionsDataLoader(DailyMissionsRepository dailyMissionsRepository) {
        this.dailyMissionsRepository = dailyMissionsRepository;
    }

    @Override
    public void run(String... args) {
        if (dailyMissionsRepository.count() == 0) { // Prevent duplicate inserts
            List<DailyMissions> dailyMissions = new ArrayList<>();

            for (int i = 1; i <= 100; i++) {
                DailyMissions dailyMission = new DailyMissions();
                dailyMission.setDayNumber(i);
                dailyMission.setTitle("Mission for Day " + i);
                dailyMission.setDescription("Complete the tasks for day " + i);
                dailyMission.setDifficulty(determineDifficulty(i));
                dailyMission.setRewardPoints(generateRewardPoints(dailyMission.getDifficulty()));
                dailyMission.setMissions(List.of("Task 1 for Day " + i, "Task 2 for Day " + i));

                dailyMissions.add(dailyMission);
            }
            dailyMissionsRepository.saveAll(dailyMissions);
        }
    }

    private Tier determineDifficulty(int day) {
        if (day <= 20) return Tier.E;
        else if (day <= 40) return Tier.D;
        else if (day <= 70) return Tier.C;
        else if (day <= 90) return Tier.B;
        else return Tier.A;
    }

    private Integer generateRewardPoints(Tier tier){
        if (tier == Tier.E) return 10;
        else if (tier == Tier.D) return 5;
        else if (tier == Tier.C) return 4;
        else if (tier == Tier.B) return 3;
        else if (tier == Tier.A) return 2;
        return 1;
    }
}

