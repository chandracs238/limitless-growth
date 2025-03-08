package com.pcs.limitless_growth.repository;

import com.pcs.limitless_growth.entities.PersonalMissions;
import com.pcs.limitless_growth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonalMissionsRepository extends JpaRepository<PersonalMissions, Long> {
    List<PersonalMissions> findByUser(User user);
}
