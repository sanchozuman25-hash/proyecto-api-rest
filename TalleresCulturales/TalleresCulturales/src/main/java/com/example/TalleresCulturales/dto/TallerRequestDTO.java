package com.example.TalleresCulturales.dto;

import com.example.TalleresCulturales.validation.FechaFinPosteriorAInicio;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@FechaFinPosteriorAInicio
public record TallerRequestDTO(

        @Schema(example = "Taller de acuarela")
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 150, message = "El título no puede superar los 150 caracteres")
        String titulo,

        @Schema(example = "2026-11-10T17:00:00")
        @NotNull(message = "La fecha y hora de inicio es obligatoria")
        LocalDateTime fechaHoraInicio,

        @Schema(example = "2026-11-10T19:00:00")
        @NotNull(message = "La fecha y hora de fin es obligatoria")
        LocalDateTime fechaHoraFin,

        @Schema(example = "1")
        @NotNull(message = "El organizador es obligatorio")
        @Positive(message = "El identificador del organizador debe ser positivo")
        Long organizadorId) {
}
