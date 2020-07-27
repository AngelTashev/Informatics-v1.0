package com.angeltashev.informatics.scheduling;

import com.angeltashev.informatics.assignment.service.AssignmentService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AssignmentScheduler {

    private final AssignmentService assignmentService;

    @Scheduled(cron = "${informatics.cleanup-schedule-time}")
    public void disableOldAssignments() {
        this.assignmentService.disableOldAssignments();
    }

    @Scheduled(cron = "${informatics.cleanup-schedule-time}")
    public void cleanUpOldAssignments() {
        this.assignmentService.cleanUpOldAssignments();
    }

    @Scheduled(cron = "${informatics.update-cache-schedule-time}")
    public void updateAllAssignments() {
        this.assignmentService.updateAllAssignments();
    }
}
