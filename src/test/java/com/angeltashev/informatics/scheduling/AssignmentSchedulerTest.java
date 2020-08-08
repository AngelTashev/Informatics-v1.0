package com.angeltashev.informatics.scheduling;

import com.angeltashev.informatics.assignment.service.AssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AssignmentSchedulerTest {

    @Mock
    private AssignmentService assignmentService;

    private AssignmentScheduler schedulerToTest;

    @BeforeEach
    void setUp() {
        this.schedulerToTest = new AssignmentScheduler(this.assignmentService);
    }

    @Test
    void disableOldAssignmentsShouldCallCorrectMethod() {
        // Arrange

        // Act
        this.schedulerToTest.disableOldAssignments();

        // Assert
        verify(this.assignmentService, times(1)).disableOldAssignments();
    }

    @Test
    void cleanUpOldAssignmentsShouldCallCorrectMethod() {
        // Arrange

        // Act
        this.schedulerToTest.cleanUpOldAssignments();

        // Assert
        verify(this.assignmentService, times(1)).cleanUpOldAssignments();
    }

    @Test
    void updateAllAssignmentsShouldCallCorrectMethod() {
        // Arrange

        // Act
        this.schedulerToTest.updateAllAssignments();

        // Assert
        verify(this.assignmentService, times(1)).updateAllAssignments();
    }
}
