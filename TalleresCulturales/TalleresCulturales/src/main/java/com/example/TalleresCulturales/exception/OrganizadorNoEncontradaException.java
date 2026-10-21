package com.example.TalleresCulturales.exception;

public class OrganizadorNoEncontradaException extends RuntimeException {

    public OrganizadorNoEncontradaException(Long id) {
        super("No existe ningún organizador con id " + id);
    }
}
