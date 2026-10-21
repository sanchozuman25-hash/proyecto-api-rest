package com.example.TalleresCulturales.exception;

public class TallerNoEncontradaException extends RuntimeException {

    public TallerNoEncontradaException(Long id) {
        super("No existe ningún taller con id " + id);
    }
}
