package com.pcs.limitless_growth.repository;

import com.pcs.limitless_growth.entities.DailyMissions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DailyMissionsRepositoryTest {

    @Mock
    private DailyMissionsRepository dailyMissionsRepository;

    @Test
    void shouldFindMissionByDayNumber(){
        DailyMissions missions = new DailyMissions();
        missions.setDayNumber(1);
        missions.setTitle("Learn Java");

        when(dailyMissionsRepository.findByDayNumber(1))
                .thenReturn(missions);

        DailyMissions result = dailyMissionsRepository.findByDayNumber(1);

        assertNotNull(result);
        assertEquals(1, result.getDayNumber());
        assertEquals("Learn Java", result.getTitle());
    }
}


