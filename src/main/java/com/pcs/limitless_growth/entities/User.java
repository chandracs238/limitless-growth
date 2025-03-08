package com.pcs.limitless_growth.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    private Tier tier;
    private Long expPoints;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserDailyMissionsProgress> dailyMissionsProgresses;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PersonalMissions> personalMissions;
}
