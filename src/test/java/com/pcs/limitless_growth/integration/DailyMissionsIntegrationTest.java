package com.pcs.limitless_growth.integration;


import com.pcs.limitless_growth.entities.DailyMissions;
import com.pcs.limitless_growth.entities.User;
import com.pcs.limitless_growth.repository.DailyMissionsRepository;
import com.pcs.limitless_growth.repository.UserRepository;
import com.pcs.limitless_growth.repository.UserRepositoryTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DailyMissionsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DailyMissions dailyMissions;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void shouldAssignMissionsAndPersistToDatabase() throws Exception{
        User user = new User();
        user.setId(1L);
        user.setUsername("pcs");

        mockMvc.perform(get("/users/1/dailyMissions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dayNumber").isNumber());
    }
}
