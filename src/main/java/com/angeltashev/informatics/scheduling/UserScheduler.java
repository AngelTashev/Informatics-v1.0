package com.angeltashev.informatics.scheduling;

import com.angeltashev.informatics.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class UserScheduler {

    private final UserService userService;

    @Scheduled(cron = "${informatics.update-cache-schedule-time}")
    public void updateCachedUsers() {
        log.info("User scheduler: Started scheduled update of cached users");
        this.userService.updateAllStudents();
    }
}
