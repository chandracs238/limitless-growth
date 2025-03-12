package com.pcs.limitless_growth.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_progress")
public class UserDailyMissionsProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    private Integer dayNumber;
    private boolean completed;
    private LocalDate assignedDate;
    private LocalDate completedAt;
}
