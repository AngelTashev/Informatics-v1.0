package com.angeltashev.informatics.user.validation;

import com.angeltashev.informatics.user.validation.annotation.Username;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsernameValidator implements ConstraintValidator<Username, String> {


    private static final String REGEX = "^[a-z0-9\\.\\_]+$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Override
    public boolean isValid(final String username, ConstraintValidatorContext context) {
        return validateUsername(username);
    }

    private boolean validateUsername(final String username) {
        Matcher matcher = PATTERN.matcher(username);
        return matcher.matches();
    }


}
