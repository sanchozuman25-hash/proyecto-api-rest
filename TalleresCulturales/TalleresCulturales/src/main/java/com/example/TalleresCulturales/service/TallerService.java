package com.example.TalleresCulturales.service;

import com.example.TalleresCulturales.dto.ParticipanteSimpleDTO;
import com.example.TalleresCulturales.dto.TallerRequestDTO;
import com.example.TalleresCulturales.dto.TallerResponseDTO;
import com.example.TalleresCulturales.exception.*;
import com.example.TalleresCulturales.mapper.Mapper;
import com.example.TalleresCulturales.model.Organizador;
import com.example.TalleresCulturales.model.Participante;
import com.example.TalleresCulturales.model.Taller;
import com.example.TalleresCulturales.repository.OrganizadorRepository;
import com.example.TalleresCulturales.repository.ParticipanteRepository;
import com.example.TalleresCulturales.repository.TallerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TallerService {

    private final TallerRepository tallerRepository;
    private final OrganizadorRepository organizadorRepository;
    private final ParticipanteRepository participanteRepository;
    private final Mapper mapper;


    public TallerResponseDTO crear(TallerRequestDTO dto) {
        String titulo = dto.titulo().strip();
        if (tallerRepository.existsByTituloIgnoreCase(titulo)) {
            throw new TallerYaExisteException(titulo);
        }
        Organizador organizador = buscarOrganizador(dto.organizadorId());
        return mapper.toResponse(tallerRepository.save(mapper.toEntity(dto, organizador)));
    }

    public List<TallerResponseDTO> listar() {
        return tallerRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    public TallerResponseDTO obtener(Long id) {
        return mapper.toResponse(buscarTaller(id));
    }

    public TallerResponseDTO actualizar(Long id, TallerRequestDTO dto) {
        Taller taller = buscarTaller(id);
        String titulo = dto.titulo().strip();
        if (tallerRepository.existsByTituloIgnoreCaseAndIdNot(titulo, id)) {
            throw new TallerYaExisteException(titulo);
        }
        mapper.update(taller, dto, buscarOrganizador(dto.organizadorId()));
        return mapper.toResponse(tallerRepository.save(taller));
    }

    public void eliminar(Long id) {
        tallerRepository.delete(buscarTaller(id));
    }


    public List<ParticipanteSimpleDTO> listarParticipantes(Long tallerId) {
        return mapper.toResponse(buscarTaller(tallerId)).participantes();
    }

    public TallerResponseDTO inscribir(Long tallerId, Long participanteId) {
        Taller taller = buscarTaller(tallerId);
        Participante participante = buscarParticipante(participanteId);
        if (!taller.getParticipantes().add(participante)) {
            throw new ParticipanteYaInscritoException(tallerId, participanteId);
        }
        return mapper.toResponse(tallerRepository.save(taller));
    }

    public void desinscribir(Long tallerId, Long participanteId) {
        Taller taller = buscarTaller(tallerId);
        buscarParticipante(participanteId);
        taller.getParticipantes().removeIf(p -> p.getId().equals(participanteId));
    }


    private Taller buscarTaller(Long id) {
        return tallerRepository.findById(id).orElseThrow(() -> new TallerNoEncontradaException(id));
    }

    private Organizador buscarOrganizador(Long id) {
        return organizadorRepository.findById(id).orElseThrow(() -> new OrganizadorNoEncontradaException(id));
    }

    private Participante buscarParticipante(Long id) {
        return participanteRepository.findById(id).orElseThrow(() -> new ParticipanteNoEncontradaException(id));
    }
}
