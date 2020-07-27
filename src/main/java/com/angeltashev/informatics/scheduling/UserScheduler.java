package com.angeltashev.informatics.scheduling;

import com.angeltashev.informatics.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserScheduler {

    private final UserService userService;

    @Scheduled(cron = "${informatics.update-cache-schedule-time}")
    public void refreshCachedUsers() {
        this.userService.updateAllStudents();
    }
}
