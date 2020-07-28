package com.angeltashev.informatics.scheduling;

import com.angeltashev.informatics.assignment.service.AssignmentService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class AssignmentScheduler {

    private final AssignmentService assignmentService;

    @Scheduled(cron = "${informatics.cleanup-schedule-time}")
    public void disableOldAssignments() {
        log.info("Assignment scheduler: Started scheduled disabling of old assignments");
        this.assignmentService.disableOldAssignments();
    }

    @Scheduled(cron = "${informatics.cleanup-schedule-time}")
    public void cleanUpOldAssignments() {
        log.info("Assignment scheduler: Started scheduled clean up of old assignments");
        this.assignmentService.cleanUpOldAssignments();
    }

    @Scheduled(cron = "${informatics.update-cache-schedule-time}")
    public void updateAllAssignments() {
        log.info("Assignment scheduler: Started scheduled update of cached assignments");
        this.assignmentService.updateAllAssignments();
    }
}
