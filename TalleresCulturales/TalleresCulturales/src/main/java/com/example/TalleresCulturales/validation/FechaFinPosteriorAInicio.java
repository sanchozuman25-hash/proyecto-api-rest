package com.example.TalleresCulturales.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FechaFinPosteriorAInicioValidator.class)
public @interface FechaFinPosteriorAInicio {

    String message() default "La fecha y hora de fin tiene que ser posterior a la fecha y hora de inicio";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
