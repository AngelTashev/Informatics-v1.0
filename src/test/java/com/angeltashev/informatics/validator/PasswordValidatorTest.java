package com.angeltashev.informatics.validator;

import com.angeltashev.informatics.user.validation.PasswordValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PasswordValidatorTest {

    private PasswordValidator validatorToTest;

    @BeforeEach
    void setUp() {
        this.validatorToTest = new PasswordValidator();
    }

    @Test
    public void isValidShouldReturnTrueForValidPassword() {
        // Arrange

        // Act
        boolean result = this.validatorToTest.isValid("valid*password$11", Mockito.mock(ConstraintValidatorContext.class));

        // Assert
        assertTrue(result);
    }

    @Test
    public void isValidShouldReturnFalseForInvalidPassword() {
        // Arrange

        // Act
        boolean result = this.validatorToTest.isValid("Invalid password 11!", Mockito.mock(ConstraintValidatorContext.class));

        // Assert
        assertFalse(result);
    }
}

