package com.tms.finalproject_autoshop.service;

import com.tms.finalproject_autoshop.annotations.CustomAge;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AgeValidator implements ConstraintValidator<CustomAge, Integer> {
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        return value != null && value >= 18 && value <= 120;
    }
}
