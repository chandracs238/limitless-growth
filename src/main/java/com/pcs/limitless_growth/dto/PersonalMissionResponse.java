package com.pcs.limitless_growth.dto;

import com.pcs.limitless_growth.entities.PersonalMissions;
import com.pcs.limitless_growth.entities.Status;
import com.pcs.limitless_growth.entities.Tier;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonalMissionResponse {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Tier difficulty;
    private Status status;
    private Integer rewardPoints;

    public PersonalMissionResponse(PersonalMissions mission) {
        this.id = mission.getId();
        this.name = mission.getName();
        this.startDate = mission.getStartDate();
        this.endDate = mission.getEndDate();
        this.difficulty = mission.getDifficulty();
        this.status = mission.getStatus();
        this.rewardPoints = mission.getRewardPoints();
    }
}
