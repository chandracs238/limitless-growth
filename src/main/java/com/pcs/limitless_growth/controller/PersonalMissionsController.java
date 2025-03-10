package com.pcs.limitless_growth.controller;

import com.pcs.limitless_growth.dto.PersonalMissionRequest;
import com.pcs.limitless_growth.dto.PersonalMissionResponse;
import com.pcs.limitless_growth.entities.PersonalMissions;
import com.pcs.limitless_growth.entities.Status;
import com.pcs.limitless_growth.entities.User;
import com.pcs.limitless_growth.exception.ResourceNotFoundException;
import com.pcs.limitless_growth.repository.UserRepository;
import com.pcs.limitless_growth.service.PersonalMissionsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/personalMissions")
public class PersonalMissionsController {

    private final PersonalMissionsService personalMissionsService;
    private final UserRepository userRepository;

    @Autowired
    public PersonalMissionsController(PersonalMissionsService personalMissionsService, UserRepository userRepository) {
        this.personalMissionsService = personalMissionsService;
        this.userRepository = userRepository;
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + userId + " not found."));
    }

    @GetMapping
    public ResponseEntity<List<PersonalMissionResponse>> getAllPersonalMissions(@PathVariable Long userId) {
        User user = findUser(userId);
        List<PersonalMissionResponse> personalMissionResponses = personalMissionsService.getAllPersonalMissions(user)
                .stream()
                .map(PersonalMissionResponse::new)
                .toList();
        return ResponseEntity.ok(personalMissionResponses);
    }

    @GetMapping("/{missionId}")
    public ResponseEntity<PersonalMissionResponse> getPersonalMissionById(@PathVariable Long userId, @PathVariable Long missionId) {
        findUser(userId); // Ensures user exists
        PersonalMissionResponse personalMissionResponse =
                new PersonalMissionResponse(personalMissionsService.getPersonalMissionById(missionId));
        return ResponseEntity.ok(personalMissionResponse);
    }

    @PostMapping
    public ResponseEntity<String> addNewPersonalMission(
            @RequestBody @Valid PersonalMissionRequest request,
            @PathVariable Long userId) {
        User user = findUser(userId);

        PersonalMissions savedMission = personalMissionsService.addNewMission(request, user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{missionId}")
                .buildAndExpand(savedMission.getId())
                .toUri();

        return ResponseEntity.created(location).body("Successfully created a personal mission");
    }


    @PutMapping("/{missionId}")
    public ResponseEntity<String> updatePersonalMission(
            @PathVariable Long userId,
            @PathVariable Long missionId,
            @RequestBody PersonalMissionRequest updatedMission) throws AccessDeniedException {
        User user = findUser(userId);
        personalMissionsService.updatePersonalMissionById(missionId, updatedMission, user);
        return ResponseEntity.ok().body("Successfully updated a personal mission");
    }


    @PatchMapping("/{missionId}/status/{status}")
    public ResponseEntity<String> updateMissionStatus(@PathVariable Long userId, @PathVariable Long missionId, @PathVariable Status status) throws AccessDeniedException {
        User user = findUser(userId);
        personalMissionsService.updateMissionStatus(missionId, status, user);
        return ResponseEntity.ok().body("Status Updated");
    }

    @DeleteMapping("/{missionId}")
    public ResponseEntity<?> deleteMission(@PathVariable Long userId, @PathVariable Long missionId) throws AccessDeniedException {
        User user = findUser(userId);
        personalMissionsService.deleteMission(missionId, user);
        return ResponseEntity.noContent().build();
    }

}

