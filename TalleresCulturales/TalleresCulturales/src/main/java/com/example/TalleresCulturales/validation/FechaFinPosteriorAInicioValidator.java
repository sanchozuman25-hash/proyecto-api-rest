package com.example.TalleresCulturales.validation;

import com.example.TalleresCulturales.dto.TallerRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FechaFinPosteriorAInicioValidator
        implements ConstraintValidator<FechaFinPosteriorAInicio, TallerRequestDTO> {

    @Override
    public boolean isValid(TallerRequestDTO dto, ConstraintValidatorContext context) {
        if (dto == null || dto.fechaHoraInicio() == null || dto.fechaHoraFin() == null) {
            return true;
        }

        boolean valido = dto.fechaHoraFin().isAfter(dto.fechaHoraInicio());

        if (!valido) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("fechaHoraFin")
                    .addConstraintViolation();
        }
        return valido;
    }
}
