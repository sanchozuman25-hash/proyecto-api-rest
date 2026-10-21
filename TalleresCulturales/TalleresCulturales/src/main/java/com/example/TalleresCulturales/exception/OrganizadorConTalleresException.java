package com.example.TalleresCulturales.exception;

public class OrganizadorConTalleresException extends RuntimeException {

    public OrganizadorConTalleresException(Long id) {
        super("No se puede eliminar el organizador " + id + " porque tiene talleres asociados");
    }
}
