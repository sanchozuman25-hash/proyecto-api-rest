package com.example.TalleresCulturales.exception;

public class ParticipanteYaInscritoException extends RuntimeException {

    public ParticipanteYaInscritoException(Long tallerId, Long participanteId) {
        super("El participante " + participanteId + " ya está inscrito en el taller " + tallerId);
    }
}
