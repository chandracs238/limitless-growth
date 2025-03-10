package com.pcs.limitless_growth.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DailyMissions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer dayNumber;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Tier difficulty;
    @ElementCollection
    private List<String> missions;
    private Integer rewardPoints;
}
