package com.example.TalleresCulturales.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({OrganizadorNoEncontradaException.class, TallerNoEncontradaException.class,
            ParticipanteNoEncontradaException.class})
    public ProblemDetail handleNoEncontrado(RuntimeException ex) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        p.setTitle("Recurso no encontrado");
        return p;
    }

    @ExceptionHandler({TallerYaExisteException.class, ParticipanteYaInscritoException.class,
            OrganizadorConTalleresException.class})
    public ProblemDetail handleConflicto(RuntimeException ex) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        p.setTitle("Conflicto");
        return p;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new TreeMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> errores.merge(e.getField(),
                Objects.requireNonNullElse(e.getDefaultMessage(), "Valor no válido"), (a, b) -> a + "; " + b));
        ProblemDetail p = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Los datos enviados no son válidos");
        p.setTitle("Error de validación");
        p.setProperty("errores", errores);
        return p;
    }
}