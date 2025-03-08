package com.pcs.limitless_growth.dto;

import com.pcs.limitless_growth.entities.Status;
import com.pcs.limitless_growth.entities.Tier;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonalMissionRequest {
    @NotBlank
    private String name;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private Tier difficulty;

    @NotNull
    private Status status;

    // Getters and Setters
}

