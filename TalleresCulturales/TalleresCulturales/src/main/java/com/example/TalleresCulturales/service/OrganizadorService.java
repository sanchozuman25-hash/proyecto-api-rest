package com.example.TalleresCulturales.service;

import com.example.TalleresCulturales.dto.OrganizadorRequestDTO;
import com.example.TalleresCulturales.dto.OrganizadorResponseDTO;
import com.example.TalleresCulturales.exception.OrganizadorConTalleresException;
import com.example.TalleresCulturales.exception.OrganizadorNoEncontradaException;
import com.example.TalleresCulturales.mapper.Mapper;
import com.example.TalleresCulturales.model.Organizador;
import com.example.TalleresCulturales.repository.OrganizadorRepository;
import com.example.TalleresCulturales.repository.TallerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganizadorService {

    private final OrganizadorRepository organizadorRepository;
    private final TallerRepository tallerRepository;
    private final Mapper mapper;

    public OrganizadorResponseDTO crear(OrganizadorRequestDTO dto) {
        return mapper.toResponse(organizadorRepository.save(mapper.toEntity(dto)));
    }

    public List<OrganizadorResponseDTO> listar() {
        return organizadorRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    public OrganizadorResponseDTO obtener(Long id) {
        return mapper.toResponse(buscar(id));
    }

    public OrganizadorResponseDTO actualizar(Long id, OrganizadorRequestDTO dto) {
        Organizador organizador = buscar(id);
        mapper.update(organizador, dto);
        return mapper.toResponse(organizadorRepository.save(organizador));
    }

    public void eliminar(Long id) {
        Organizador organizador = buscar(id);
        if (tallerRepository.existsByOrganizadorId(id)) {
            throw new OrganizadorConTalleresException(id);
        }
        organizadorRepository.delete(organizador);
    }

    private Organizador buscar(Long id) {
        return organizadorRepository.findById(id).orElseThrow(() -> new OrganizadorNoEncontradaException(id));
    }
}
