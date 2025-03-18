package com.pcs.limitless_growth.service;

import com.pcs.limitless_growth.dto.DailyMissionsProgressResponse;
import com.pcs.limitless_growth.entities.DailyMissions;
import com.pcs.limitless_growth.entities.User;
import com.pcs.limitless_growth.entities.UserDailyMissionsProgress;
import com.pcs.limitless_growth.exception.NoMissionsAvailableException;
import com.pcs.limitless_growth.repository.DailyMissionsRepository;
import com.pcs.limitless_growth.repository.UserDailyMissionsProgressRepository;
import com.pcs.limitless_growth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DailyMissionsServiceTest {

    @Mock
    private DailyMissionsRepository dailyMissionsRepository;

    @Mock
    private UserDailyMissionsProgressRepository userDailyMissionsProgressRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DailyMissionsService dailyMissionsService;

    private User user;
    private DailyMissions dailyMissions;
    private UserDailyMissionsProgress userDailyMissionsProgress;

    @BeforeEach
    void setUp(){
        user = new User();
        user.setId(1L);
        user.setExpPoints(500L);

        dailyMissions = new DailyMissions();
        dailyMissions.setDayNumber(1);
        dailyMissions.setRewardPoints(100);

        userDailyMissionsProgress = new UserDailyMissionsProgress();
        userDailyMissionsProgress.setUser(user);
        userDailyMissionsProgress.setDayNumber(1);
        userDailyMissionsProgress.setCompleted(false);
    }

    @Test
    void testCompleteDailyMissions_Success(){
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(dailyMissionsRepository.findByDayNumber(1)).thenReturn(dailyMissions);
        when(userDailyMissionsProgressRepository.findByUserAndDayNumber(user, dailyMissions.getDayNumber()))
                .thenReturn(Optional.of(userDailyMissionsProgress));

        DailyMissionsProgressResponse dailyMissionsProgressResponse = dailyMissionsService.completeDailyMissions(1L, 1);

        assertThat(dailyMissionsProgressResponse).isNotNull();
        assertThat(user.getExpPoints()).isEqualTo(600);
        assertThat(userDailyMissionsProgress.isCompleted()).isTrue();
        assertThat(userDailyMissionsProgress.getCompletedAt()).isEqualTo(LocalDate.now());

        verify(userRepository, times(1)).save(user);
        verify(userDailyMissionsProgressRepository, times(1)).save(userDailyMissionsProgress);

    }

    @Test
    void testCompleteDailyMissions_UserNotFound(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class,
                () -> dailyMissionsService.completeDailyMissions(1L, 1));

        assertThat(exception.getMessage()).isEqualTo("User not found");
    }

    @Test
    void testCompleteDailyMissions_AlreadyCompleted(){
        userDailyMissionsProgress.setCompleted(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(dailyMissionsRepository.findByDayNumber(1)).thenReturn(dailyMissions);
        when(userDailyMissionsProgressRepository.findByUserAndDayNumber(user, 1))
                .thenReturn(Optional.of(userDailyMissionsProgress));

        Exception exception = assertThrows(RuntimeException.class,
                () -> dailyMissionsService.completeDailyMissions(1L, 1));

        assertThat(exception.getMessage()).isEqualTo("Mission already completed");
    }

    @Test
    void testGetTodaysMission_shouldReturnExistingMissionIfAlreadyAssigned(){
        UserDailyMissionsProgress existingMission = new UserDailyMissionsProgress(null, user, 3, false, LocalDate.now(), null);
        DailyMissions dailyMissions1 = new DailyMissions();
        dailyMissions1.setDayNumber(3);
        dailyMissions1.setTitle("Learn Spring boot");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userDailyMissionsProgressRepository.findByUserAndAssignedDate(user, LocalDate.now()))
                .thenReturn(Optional.of(existingMission));
        when(dailyMissionsRepository.findByDayNumber(existingMission.getDayNumber()))
                .thenReturn(dailyMissions1);

        DailyMissions result = dailyMissionsService.getTodaysMission(1L);

        assertNotNull(result);
        assertEquals(3, result.getDayNumber());
    }

    @Test
    void testGetTodaysMission_shouldReturnNewMissionIfNoExistingMission(){
        UserDailyMissionsProgress existingMission = new UserDailyMissionsProgress
                (null, user, 2, false, LocalDate.now().minusDays(1), null);
        DailyMissions dailyMissions1 = new DailyMissions();
        dailyMissions1.setDayNumber(3);
        dailyMissions1.setTitle("Learn Java");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userDailyMissionsProgressRepository.findByUserAndAssignedDate(user, LocalDate.now()))
                .thenReturn(Optional.empty());
        when(userDailyMissionsProgressRepository.findTopByUserAndCompletedOrderByDayNumberDesc(user, true))
                .thenReturn(Optional.of(existingMission));
        when(dailyMissionsRepository.findByDayNumber(3)).thenReturn(dailyMissions1);

        DailyMissions result = dailyMissionsService.getTodaysMission(1L);

        assertNotNull(result);
        assertEquals(3, result.getDayNumber());
        verify(userDailyMissionsProgressRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should start form Day 1 if no missions have been completed")
    void testGetTodaysMission_shouldStartFromDayOneIfNoMissionsCompleted(){
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userDailyMissionsProgressRepository.findByUserAndAssignedDate(user, LocalDate.now()))
                .thenReturn(Optional.empty());
        when(userDailyMissionsProgressRepository.findTopByUserAndCompletedOrderByDayNumberDesc(user, true))
                .thenReturn(Optional.empty());
        when(dailyMissionsRepository.findByDayNumber(1)).thenReturn(dailyMissions);

        DailyMissions result = dailyMissionsService.getTodaysMission(1L);

        assertNotNull(result);
        assertEquals(1, result.getDayNumber());
        verify(dailyMissionsRepository, times(1)).findByDayNumber(1);
        verify(userDailyMissionsProgressRepository, times(1)).save(any());
    }

    @Test
    void testGetTodaysMission_shouldThrowExceptionIfUserNotFound(){
        when(userRepository.findById(100L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class,
                () -> dailyMissionsService.getTodaysMission(100L));

        assertThat(exception.getMessage()).isEqualTo("User not found");
    }

    @Test
    void testGetTodaysMission_shouldThrowExceptionIfNoNewMissionsAreAvailable(){
        DailyMissions dailyMissions1 = new DailyMissions();
        dailyMissions1.setDayNumber(100);
        dailyMissions1.setTitle("Learn Java");

        UserDailyMissionsProgress userDailyMissionsProgress1 = new UserDailyMissionsProgress();
        userDailyMissionsProgress1.setUser(user);
        userDailyMissionsProgress1.setDayNumber(100);
        userDailyMissionsProgress1.setCompleted(true);


        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userDailyMissionsProgressRepository.findByUserAndAssignedDate(user, LocalDate.now()))
                .thenReturn(Optional.empty());
        when(userDailyMissionsProgressRepository.findTopByUserAndCompletedOrderByDayNumberDesc(user, true))
                .thenReturn(Optional.of(userDailyMissionsProgress1));
        when(dailyMissionsRepository.findByDayNumber(101))
                .thenThrow(new NoMissionsAvailableException("No new missions available for day 101"));

        NoMissionsAvailableException exception = assertThrows(
                NoMissionsAvailableException.class,
                () -> dailyMissionsService.getTodaysMission(1L)
        );

        assertEquals("No new missions available for day 101", exception.getMessage());

        verify(dailyMissionsRepository, times(1)).findByDayNumber(101);

    }
}
