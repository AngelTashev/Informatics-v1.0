package com.angeltashev.informatics.scheduling;

import com.angeltashev.informatics.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserSchedulerTest {

    @Mock
    private UserService userService;

    private UserScheduler schedulerToTest;

    @BeforeEach
    void setUp() {
        this.schedulerToTest = new UserScheduler(this.userService);
    }

    @Test
    void updateCachedUsersShouldCallCorrectMethod() {
        // Arrange

        // Act
        this.schedulerToTest.updateCachedUsers();

        // Assert
        verify(this.userService, times(1)).updateAllStudents();
    }
}
