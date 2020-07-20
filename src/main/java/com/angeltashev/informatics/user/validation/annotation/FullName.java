package com.angeltashev.informatics.user.validation.annotation;

import com.angeltashev.informatics.user.validation.FullNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = FullNameValidator.class)

public @interface FullName {

    String message() default "Invalid full name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
