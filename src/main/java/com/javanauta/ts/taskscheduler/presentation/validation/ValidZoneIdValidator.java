package com.javanauta.ts.taskscheduler.presentation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.ZoneId;

public class ValidZoneIdValidator implements ConstraintValidator<ValidZoneId, String> {

    @Override
    public boolean isValid(String zoneIdStr, ConstraintValidatorContext context) {
        return zoneIdStr == null || zoneIdStr.isBlank() || ZoneId.getAvailableZoneIds().contains(zoneIdStr);
    }
}