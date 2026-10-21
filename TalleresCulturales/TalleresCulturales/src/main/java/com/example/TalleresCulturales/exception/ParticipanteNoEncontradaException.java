package com.example.TalleresCulturales.exception;

public class ParticipanteNoEncontradaException extends RuntimeException {

    public ParticipanteNoEncontradaException(Long id) {
        super("No existe ningún participante con id " + id);
    }
}