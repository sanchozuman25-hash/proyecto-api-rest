package com.example.TalleresCulturales.controller;

import com.example.TalleresCulturales.config.ApiDocs;
import com.example.TalleresCulturales.dto.ParticipanteRequestDTO;
import com.example.TalleresCulturales.dto.ParticipanteSimpleDTO;
import com.example.TalleresCulturales.service.ParticipanteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/participantes")
@RequiredArgsConstructor
@Tag(name = "Participantes", description = "Gestión básica de participantes (crear y listar)")
public class ParticipanteController {

    private final ParticipanteService participanteService;

    @PostMapping
    @Operation(summary = "Crear un participante",
            description = "Crea un participante. Para inscribirlo en un taller usa POST /talleres/{tallerId}/participantes/{participanteId}.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Participante creado",
                    content = @Content(schema = @Schema(implementation = ParticipanteSimpleDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos",
                    content = @Content(mediaType = ApiDocs.PROBLEM_JSON,
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<ParticipanteSimpleDTO> crear(@Valid @RequestBody ParticipanteRequestDTO dto) {
        ParticipanteSimpleDTO creado = participanteService.crear(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.id()).toUri();
        return ResponseEntity.created(location).body(creado);
    }

    @GetMapping
    @Operation(summary = "Listar participantes",
            description = "Devuelve todos los participantes. Lista vacía si no hay ninguno.")
    @ApiResponse(responseCode = "200", description = "Listado de participantes",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ParticipanteSimpleDTO.class))))
    public List<ParticipanteSimpleDTO> listar() {
        return participanteService.listar();
    }
}
