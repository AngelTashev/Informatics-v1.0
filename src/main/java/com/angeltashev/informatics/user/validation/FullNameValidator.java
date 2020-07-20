package com.angeltashev.informatics.user.validation;

import com.angeltashev.informatics.user.validation.annotation.FullName;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FullNameValidator implements ConstraintValidator<FullName, String> {

    private static final String REGEX = "^[A-Z][A-Za-z\\. ]+$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Override
    public boolean isValid(final String fullName, ConstraintValidatorContext context) {
        return validateFullName(fullName);
    }

    private boolean validateFullName(final String fullName) {
        Matcher matcher = PATTERN.matcher(fullName);
        return matcher.matches();
    }
}
