package com.example.TalleresCulturales.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TallerResponseDTO(
        Long id,
        String titulo,
        LocalDateTime fechaHoraInicio,
        LocalDateTime fechaHoraFin,
        OrganizadorSimpleDTO organizador,
        List<ParticipanteSimpleDTO> participantes) {
}
