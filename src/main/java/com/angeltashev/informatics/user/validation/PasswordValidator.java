package com.angeltashev.informatics.user.validation;

import com.angeltashev.informatics.user.validation.annotation.Password;
import com.angeltashev.informatics.user.validation.annotation.Username;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<Password, String> {


    private static final String REGEX = "^[\\$\\*\\.A-Za-z0-9]+$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Override
    public boolean isValid(final String password, ConstraintValidatorContext context) {
        return validatePassword(password);
    }

    private boolean validatePassword(final String password) {
        Matcher matcher = PATTERN.matcher(password);
        return matcher.matches();
    }


}
