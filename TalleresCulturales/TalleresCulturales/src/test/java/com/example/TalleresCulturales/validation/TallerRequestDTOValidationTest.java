package com.example.TalleresCulturales.validation;

import com.example.TalleresCulturales.dto.TallerRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TallerRequestDTOValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    private final LocalDateTime inicio = LocalDateTime.of(2026, 11, 10, 17, 0);

    @BeforeAll
    static void iniciar() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void cerrar() {
        factory.close();
    }

    @Test
    void dtoValidoNoGeneraErrores() {
        var dto = new TallerRequestDTO("Acuarela", inicio, inicio.plusHours(2), 1L);
        assertThat(validator.validate(dto)).isEmpty();
    }

    @Test
    void finIgualAInicioEsInvalido() {
        var dto = new TallerRequestDTO("Acuarela", inicio, inicio, 1L);
        Set<ConstraintViolation<TallerRequestDTO>> errores = validator.validate(dto);
        assertThat(errores).hasSize(1);
        assertThat(errores.iterator().next().getPropertyPath().toString()).isEqualTo("fechaHoraFin");
    }

    @Test
    void finAnteriorAInicioEsInvalido() {
        var dto = new TallerRequestDTO("Acuarela", inicio, inicio.minusMinutes(1), 1L);
        assertThat(validator.validate(dto)).hasSize(1);
    }

    @Test
    void tituloEnBlancoYOrganizadorNuloSonInvalidos() {
        var dto = new TallerRequestDTO("   ", inicio, inicio.plusHours(1), null);
        assertThat(validator.validate(dto)).hasSize(2);
    }

    @Test
    void faltaDeFechasSoloGeneraErroresDeNotNull() {
        var dto = new TallerRequestDTO("Acuarela", null, null, 1L);
        assertThat(validator.validate(dto)).hasSize(2);
    }
}
