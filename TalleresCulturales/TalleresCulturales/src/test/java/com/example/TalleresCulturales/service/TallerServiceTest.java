package com.example.TalleresCulturales.service;

import com.example.TalleresCulturales.dto.TallerRequestDTO;
import com.example.TalleresCulturales.exception.ParticipanteYaInscritoException;
import com.example.TalleresCulturales.exception.TallerYaExisteException;
import com.example.TalleresCulturales.mapper.OrganizadorMapper;
import com.example.TalleresCulturales.mapper.ParticipanteMapper;
import com.example.TalleresCulturales.mapper.TallerMapper;
import com.example.TalleresCulturales.model.Organizador;
import com.example.TalleresCulturales.model.Participante;
import com.example.TalleresCulturales.model.Taller;
import com.example.TalleresCulturales.repository.OrganizadorRepository;
import com.example.TalleresCulturales.repository.ParticipanteRepository;
import com.example.TalleresCulturales.repository.TallerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TallerServiceTest {

    @Mock
    private TallerRepository tallerRepository;
    @Mock
    private OrganizadorRepository organizadorRepository;
    @Mock
    private ParticipanteRepository participanteRepository;

    private TallerService service;

    private final LocalDateTime inicio = LocalDateTime.of(2026, 11, 10, 17, 0);

    @BeforeEach
    void preparar() {
        TallerMapper mapper = new TallerMapper(new OrganizadorMapper(), new ParticipanteMapper());
        service = new TallerService(tallerRepository, organizadorRepository, participanteRepository, mapper);
    }

    private Organizador organizador() {
        Organizador o = new Organizador();
        o.setId(1L);
        o.setNombre("Centro Cultural");
        o.setEmail("info@centro.es");
        return o;
    }

    private Taller taller(Long id, String titulo) {
        Taller t = new Taller();
        t.setId(id);
        t.setTitulo(titulo);
        t.setFechaHoraInicio(inicio);
        t.setFechaHoraFin(inicio.plusHours(2));
        t.setOrganizador(organizador());
        return t;
    }

    private Participante participante(Long id) {
        Participante p = new Participante();
        p.setId(id);
        p.setNombreCompleto("Lucía Fernández");
        return p;
    }

    @Test
    void crearConTituloDeOtroTallerIgnorandoMayusculasYEspaciosLanzaExcepcion() {
        // El servicio normaliza "  ACUARELA " -> "ACUARELA" y compara sin distinguir mayúsculas.
        when(tallerRepository.existsByTituloIgnoreCase("ACUARELA")).thenReturn(true);
        var dto = new TallerRequestDTO("  ACUARELA ", inicio, inicio.plusHours(2), 1L);

        assertThatThrownBy(() -> service.crear(dto)).isInstanceOf(TallerYaExisteException.class);
    }

    @Test
    void modificarConservandoElTituloPropioNoLanzaExcepcion() {
        Taller existente = taller(1L, "Acuarela");
        when(tallerRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(tallerRepository.existsByTituloIgnoreCaseAndIdNot("acuarela", 1L)).thenReturn(false);
        when(organizadorRepository.findById(1L)).thenReturn(Optional.of(organizador()));
        var dto = new TallerRequestDTO(" acuarela ", inicio, inicio.plusHours(3), 1L);

        assertThatCode(() -> service.actualizar(1L, dto)).doesNotThrowAnyException();
        assertThat(existente.getTitulo()).isEqualTo("acuarela");
    }

    @Test
    void inscribirDosVecesAlMismoParticipanteLanzaExcepcion() {
        Taller t = taller(1L, "Acuarela");
        Participante p = participante(5L);
        t.getParticipantes().add(p);
        when(tallerRepository.findById(1L)).thenReturn(Optional.of(t));
        when(participanteRepository.findById(5L)).thenReturn(Optional.of(p));

        assertThatThrownBy(() -> service.inscribir(1L, 5L)).isInstanceOf(ParticipanteYaInscritoException.class);
    }

    @Test
    void desinscribirSinVinculoTerminaSinCambios() {
        Taller t = taller(1L, "Acuarela");
        when(tallerRepository.findById(1L)).thenReturn(Optional.of(t));
        when(participanteRepository.findById(5L)).thenReturn(Optional.of(participante(5L)));

        assertThatCode(() -> service.desinscribir(1L, 5L)).doesNotThrowAnyException();
        assertThat(t.getParticipantes()).isEmpty();
    }

    @Test
    void desinscribirEliminaSoloLaInscripcion() {
        Taller t = taller(1L, "Acuarela");
        Participante p = participante(5L);
        t.getParticipantes().add(p);
        when(tallerRepository.findById(1L)).thenReturn(Optional.of(t));
        when(participanteRepository.findById(5L)).thenReturn(Optional.of(p));

        service.desinscribir(1L, 5L);

        assertThat(t.getParticipantes()).isEmpty();
    }
}
