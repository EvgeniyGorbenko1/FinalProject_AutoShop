package com.tms.finalproject_autoshop.annotations;

import com.tms.finalproject_autoshop.service.AgeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = AgeValidator.class)
public @interface CustomAge {
    String message() default "Age must be between 18 and 120 years old!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}