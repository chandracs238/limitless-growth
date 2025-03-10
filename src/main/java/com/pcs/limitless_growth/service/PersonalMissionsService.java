package com.pcs.limitless_growth.service;

import com.pcs.limitless_growth.dto.PersonalMissionRequest;
import com.pcs.limitless_growth.entities.PersonalMissions;
import com.pcs.limitless_growth.entities.Status;
import com.pcs.limitless_growth.entities.User;
import com.pcs.limitless_growth.exception.ResourceNotFoundException;
import com.pcs.limitless_growth.repository.PersonalMissionsRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class PersonalMissionsService {

    private final PersonalMissionsRepository personalMissionsRepository;

    @Autowired
    public PersonalMissionsService(PersonalMissionsRepository personalMissionsRepository) {
        this.personalMissionsRepository = personalMissionsRepository;
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
                .orElseThrow(() -> new ResourceNotFoundException("Mission with ID " + id + " not found."));
    }

    public PersonalMissions updatePersonalMissionById(Long id, PersonalMissionRequest updatedMission, User user) throws AccessDeniedException {
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

        return personalMissionsRepository.save(existingMission);
    }

    public PersonalMissions updateMissionStatus(Long missionId, Status status, User user) throws AccessDeniedException {
        PersonalMissions existingMission = getPersonalMissionById(missionId);
        if (!existingMission.getUser().equals(user)) {
            throw new AccessDeniedException("You do not have permission to modify this mission.");
        }
        existingMission.setStatus(status);
        return personalMissionsRepository.save(existingMission);
    }

    public void deleteMission(Long missionId, User user) throws AccessDeniedException {
        PersonalMissions existingMission = getPersonalMissionById(missionId);
        if (!existingMission.getUser().equals(user)) {
            throw new AccessDeniedException("You do not have permission to Delete this mission.");
        }
        personalMissionsRepository.delete(existingMission);
    }
}
