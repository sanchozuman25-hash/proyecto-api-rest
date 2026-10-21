package com.example.TalleresCulturales.service;

import com.example.TalleresCulturales.dto.ParticipanteRequestDTO;
import com.example.TalleresCulturales.dto.ParticipanteSimpleDTO;
import com.example.TalleresCulturales.mapper.Mapper;
import com.example.TalleresCulturales.repository.ParticipanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipanteService {

    private final ParticipanteRepository participanteRepository;
    private final Mapper mapper;

    public ParticipanteSimpleDTO crear(ParticipanteRequestDTO dto) {
        return mapper.toSimple(participanteRepository.save(mapper.toEntity(dto)));
    }

    public List<ParticipanteSimpleDTO> listar() {
        return participanteRepository.findAll().stream().map(mapper::toSimple).toList();
    }
}
