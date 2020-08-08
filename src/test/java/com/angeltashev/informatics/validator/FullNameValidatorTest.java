package com.angeltashev.informatics.validator;

import com.angeltashev.informatics.user.validation.FullNameValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FullNameValidatorTest {

    private FullNameValidator validatorToTest;

    @BeforeEach
    void setUp() {
        this.validatorToTest = new FullNameValidator();
    }

    @Test
    public void isValidShouldReturnTrueForValidFullName() {
        // Arrange

        // Act
        boolean result = this.validatorToTest.isValid("Valid Full Name", Mockito.mock(ConstraintValidatorContext.class));

        // Assert
        assertTrue(result);
    }

    @Test
    public void isValidShouldReturnFalseForInvalidFullName() {
        // Arrange

        // Act
        boolean result = this.validatorToTest.isValid("invalid Full Name", Mockito.mock(ConstraintValidatorContext.class));

        // Assert
        assertFalse(result);
    }
}
