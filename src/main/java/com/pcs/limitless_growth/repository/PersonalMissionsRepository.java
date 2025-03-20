package com.pcs.limitless_growth.repository;

import com.pcs.limitless_growth.entities.PersonalMissions;
import com.pcs.limitless_growth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonalMissionsRepository extends JpaRepository<PersonalMissions, Long> {
    List<PersonalMissions> findByUser(User user);
}
