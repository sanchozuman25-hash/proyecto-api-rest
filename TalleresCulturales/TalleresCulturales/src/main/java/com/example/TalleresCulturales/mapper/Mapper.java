package com.example.TalleresCulturales.mapper;

import com.example.TalleresCulturales.dto.*;
import com.example.TalleresCulturales.model.Organizador;
import com.example.TalleresCulturales.model.Participante;
import com.example.TalleresCulturales.model.Taller;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class Mapper {

    public Organizador toEntity(OrganizadorRequestDTO dto) {
        Organizador o = new Organizador();
        update(o, dto);
        return o;
    }

    public void update(Organizador o, OrganizadorRequestDTO dto) {
        o.setNombre(dto.nombre().strip());
        o.setEmail(dto.email().strip());
    }

    public OrganizadorResponseDTO toResponse(Organizador o) {
        return new OrganizadorResponseDTO(o.getId(), o.getNombre(), o.getEmail());
    }

    public OrganizadorSimpleDTO toSimple(Organizador o) {
        return new OrganizadorSimpleDTO(o.getId(), o.getNombre());
    }

    public Participante toEntity(ParticipanteRequestDTO dto) {
        Participante p = new Participante();
        p.setNombreCompleto(dto.nombreCompleto().strip());
        return p;
    }

    public ParticipanteSimpleDTO toSimple(Participante p) {
        return new ParticipanteSimpleDTO(p.getId(), p.getNombreCompleto());
    }

    public Taller toEntity(TallerRequestDTO dto, Organizador organizador) {
        Taller t = new Taller();
        update(t, dto, organizador);
        return t;
    }

    public void update(Taller t, TallerRequestDTO dto, Organizador organizador) {
        t.setTitulo(dto.titulo().strip());
        t.setFechaHoraInicio(dto.fechaHoraInicio());
        t.setFechaHoraFin(dto.fechaHoraFin());
        t.setOrganizador(organizador);
    }

    public TallerResponseDTO toResponse(Taller t) {
        List<ParticipanteSimpleDTO> participantes = t.getParticipantes() == null
                ? List.of()
                : t.getParticipantes().stream()
                .sorted(Comparator.comparing(Participante::getId))
                .map(this::toSimple)
                .toList();
        return new TallerResponseDTO(t.getId(), t.getTitulo(), t.getFechaHoraInicio(), t.getFechaHoraFin(),
                toSimple(t.getOrganizador()), participantes);
    }
}
