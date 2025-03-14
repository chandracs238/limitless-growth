package com.pcs.limitless_growth.dataload;

import com.pcs.limitless_growth.entities.DailyMissions;
import com.pcs.limitless_growth.entities.Tier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class DailyMissionsRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        List<DailyMissions> missions = generateMissions();
        missions.forEach(System.out::println);
    }

    private List<DailyMissions> generateMissions() {
        List<DailyMissions> missionsList = new ArrayList<>();
        Tier[] tiers = {Tier.E, Tier.D, Tier.C, Tier.B, Tier.A, Tier.S, Tier.SS};

        for (int i = 1; i <= 100; i++) {
            DailyMissions mission = new DailyMissions();
            mission.setId((long) i);
            mission.setDayNumber(i);
            mission.setTitle("Day " + i + " Challenge");
            mission.setDescription("Complete the exercises to earn rewards!");

            int pushups = 10 + (i * 2);
            int situps = 15 + (i * 2);
            int crunches = 12 + (i * 2);

            mission.setMissions(Arrays.asList(
                    pushups + " Push-ups",
                    situps + " Sit-ups",
                    crunches + " Crunches"
            ));

            mission.setDifficulty(tiers[(i - 1) % tiers.length]);
            mission.setRewardPoints(10 * i);

            missionsList.add(mission);
        }
        return missionsList;
    }
}
