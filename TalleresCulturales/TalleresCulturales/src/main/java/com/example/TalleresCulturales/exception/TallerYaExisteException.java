package com.example.TalleresCulturales.exception;

public class TallerYaExisteException extends RuntimeException {

    public TallerYaExisteException(String titulo) {
        super("Ya existe otro taller con el título '" + titulo + "'");
    }
}
