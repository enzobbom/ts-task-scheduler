package com.javanauta.ts.taskscheduler.presentation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.RECORD_COMPONENT, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidZoneIdValidator.class)
public @interface ValidZoneId {
    String message() default "invalid Time Zone ID";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}