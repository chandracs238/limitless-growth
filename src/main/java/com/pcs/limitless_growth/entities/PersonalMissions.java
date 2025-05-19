package com.pcs.limitless_growth.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PersonalMissions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    private Tier difficulty;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Integer rewardPoints;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    public static Integer calculateRewardPoints(Tier tier) {
        Map<Tier, Integer> pointsMap = Map.of(
                Tier.E, 1,
                Tier.D, 2,
                Tier.C, 3,
                Tier.B, 4,
                Tier.A, 5
        );
        return pointsMap.getOrDefault(tier, 0);
    }
}
