package com.javanauta.ts.taskscheduler.presentation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.DateTimeException;
import java.time.ZoneId;

public class ValidZoneIdValidator implements ConstraintValidator<ValidZoneId, String> {

    @Override
    public boolean isValid(String zoneId, ConstraintValidatorContext context) {
        if (zoneId == null || zoneId.isBlank()) {
            return true;
        }

        try {
            ZoneId.of(zoneId);
            return true;
        } catch (DateTimeException ex) {
            return false;
        }
    }
}