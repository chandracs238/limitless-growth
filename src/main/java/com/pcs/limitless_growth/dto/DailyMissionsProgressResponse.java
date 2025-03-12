package com.pcs.limitless_growth.dto;

import com.pcs.limitless_growth.entities.UserDailyMissionsProgress;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DailyMissionsProgressResponse {
    private Integer dayNumber;
    private boolean completed;
    private LocalDate assignedDate;
    private LocalDate completedAt;

    public DailyMissionsProgressResponse(UserDailyMissionsProgress userDailyMissionsProgress){
        this.dayNumber = userDailyMissionsProgress.getDayNumber();
        this.completed = userDailyMissionsProgress.isCompleted();
        this.assignedDate = userDailyMissionsProgress.getAssignedDate();
        this.completedAt = userDailyMissionsProgress.getCompletedAt();
    }
}
