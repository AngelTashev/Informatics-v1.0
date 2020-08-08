package com.angeltashev.informatics.validator;

import com.angeltashev.informatics.user.validation.UsernameValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UsernameValidatorTest {

    private UsernameValidator validatorToTest;

    @BeforeEach
    void setUp() {
        this.validatorToTest = new UsernameValidator();
    }

    @Test
    public void isValidShouldReturnTrueForValidUsername() {
        // Arrange

        // Act
        boolean result = this.validatorToTest.isValid("valid.username_valid", Mockito.mock(ConstraintValidatorContext.class));

        // Assert
        assertTrue(result);
    }

    @Test
    public void isValidShouldReturnFalseForInvalidUsername() {
        // Arrange

        // Act
        boolean result = this.validatorToTest.isValid("invalid username-valid", Mockito.mock(ConstraintValidatorContext.class));

        // Assert
        assertFalse(result);
    }
}

