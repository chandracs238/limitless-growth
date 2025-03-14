package com.pcs.limitless_growth.controller;


import com.pcs.limitless_growth.dto.DailyMissionsProgressResponse;
import com.pcs.limitless_growth.entities.DailyMissions;
import com.pcs.limitless_growth.entities.UserDailyMissionsProgress;
import com.pcs.limitless_growth.exception.NoMissionsAvailableException;
import com.pcs.limitless_growth.service.DailyMissionsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DailyMissionsController.class)
public class DailyMissionsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DailyMissionsService dailyMissionsService;

    @Test
    void shouldReturnDailyMissionForUser() throws Exception{
        DailyMissions dailyMissions = new DailyMissions();
        dailyMissions.setDayNumber(1);
        dailyMissions.setTitle("Learn Kotlin");

        when(dailyMissionsService.getTodaysMission(1L)).thenReturn(dailyMissions);

        mockMvc.perform(get("/users/1/dailyMissions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dayNumber").value(1))
                .andExpect(jsonPath("$.title").value("Learn Kotlin"));
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExists() throws Exception{
        when(dailyMissionsService.getTodaysMission(1L))
                .thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(get("/users/1/dailyMissions"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCompleteMissionSuccessfully() throws Exception{
        UserDailyMissionsProgress progress = new UserDailyMissionsProgress();
        progress.setDayNumber(10);
        progress.setCompleted(true);
        DailyMissionsProgressResponse response = new DailyMissionsProgressResponse(progress);

        when(dailyMissionsService.completeDailyMissions(1L, 10))
                .thenReturn(response);

        mockMvc.perform(post("/users/1/dailyMissions/complete/3"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestForInvalidMissionCompletion() throws Exception{
        when(dailyMissionsService.completeDailyMissions(1L, 5))
                .thenThrow(new NoMissionsAvailableException("Mission not found"));

        mockMvc.perform(post("/users/1/dailyMissions/complete/5"))
                .andExpect(status().isBadRequest());
    }

}
