package com.pcs.limitless_growth.service;

import com.pcs.limitless_growth.dto.PersonalMissionRequest;
import com.pcs.limitless_growth.entities.PersonalMissions;
import com.pcs.limitless_growth.entities.Status;
import com.pcs.limitless_growth.entities.User;
import com.pcs.limitless_growth.exception.NoMissionsAvailableException;
import com.pcs.limitless_growth.repository.PersonalMissionsRepository;
import com.pcs.limitless_growth.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class PersonalMissionsService {

    private final PersonalMissionsRepository personalMissionsRepository;
    private final UserRepository userRepository;

    @Autowired
    public PersonalMissionsService(PersonalMissionsRepository personalMissionsRepository, UserRepository userRepository) {
        this.personalMissionsRepository = personalMissionsRepository;
        this.userRepository = userRepository;
    }

    public PersonalMissions addNewMission(@Valid PersonalMissionRequest request, User user) {
        PersonalMissions personalMission = new PersonalMissions();
        personalMission.setStatus(request.getStatus());
        personalMission.setName(request.getName());
        personalMission.setStartDate(request.getStartDate());
        personalMission.setEndDate(request.getEndDate());
        personalMission.setDifficulty(request.getDifficulty());
        personalMission.setUser(user);
        personalMission.setRewardPoints(PersonalMissions.calculateRewardPoints(personalMission.getDifficulty()));
        return personalMissionsRepository.save(personalMission);
    }

    public List<PersonalMissions> getAllPersonalMissions(User user) {
        return personalMissionsRepository.findByUser(user);
    }

    public PersonalMissions getPersonalMissionById(Long id) {
        return personalMissionsRepository.findById(id)
                .orElseThrow(() -> new NoMissionsAvailableException("Mission with ID " + id + " not found."));
    }

    public void updatePersonalMissionById(Long id, PersonalMissionRequest updatedMission, User user) throws AccessDeniedException {
        PersonalMissions existingMission = getPersonalMissionById(id);

        if (!existingMission.getUser().equals(user)) {
            throw new AccessDeniedException("You do not have permission to modify this mission.");
        }

        existingMission.setName(updatedMission.getName());
        existingMission.setStartDate(updatedMission.getStartDate());
        existingMission.setEndDate(updatedMission.getEndDate());
        existingMission.setDifficulty(updatedMission.getDifficulty());
        existingMission.setStatus(updatedMission.getStatus());
        existingMission.setRewardPoints(PersonalMissions.calculateRewardPoints(updatedMission.getDifficulty()));

        personalMissionsRepository.save(existingMission);
    }

    public void updateMissionStatus(Long missionId, Status status, User user) throws AccessDeniedException {
        PersonalMissions existingMission = getPersonalMissionById(missionId);
        if (!existingMission.getUser().equals(user)) {
            throw new AccessDeniedException("You do not have permission to modify this mission.");
        }
        existingMission.setStatus(status);
        if (status.equals(Status.COMPLETED)){
            user.setExpPoints(user.getExpPoints() + existingMission.getRewardPoints());
            userRepository.save(user);
        }
        personalMissionsRepository.save(existingMission);
    }

    public void deleteMission(Long missionId, User user) throws AccessDeniedException {
        PersonalMissions existingMission = getPersonalMissionById(missionId);
        if (!existingMission.getUser().equals(user)) {
            throw new AccessDeniedException("You do not have permission to Delete this mission.");
        }
        personalMissionsRepository.delete(existingMission);
    }
}
