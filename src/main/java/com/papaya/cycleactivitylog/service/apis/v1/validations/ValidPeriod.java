package com.papaya.cycleactivitylog.service.apis.v1.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, PARAMETER, TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PeriodValidator.class)
@Documented
public @interface ValidPeriod {

    String message() default "Not a valid Period";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}