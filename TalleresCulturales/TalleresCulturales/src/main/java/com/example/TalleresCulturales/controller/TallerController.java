package com.example.TalleresCulturales.controller;

import com.example.TalleresCulturales.config.ApiDocs;
import com.example.TalleresCulturales.dto.ParticipanteSimpleDTO;
import com.example.TalleresCulturales.dto.TallerRequestDTO;
import com.example.TalleresCulturales.dto.TallerResponseDTO;
import com.example.TalleresCulturales.service.TallerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/talleres")
@RequiredArgsConstructor
@Tag(name = "Talleres", description = "Gestión de talleres (CRUD completo) y de sus inscripciones de participantes")
public class TallerController {

    private final TallerService tallerService;


    @PostMapping
    @Operation(summary = "Crear un taller",
            description = "Crea un taller asociado a un organizador existente. El título debe ser único "
                    + "(se ignoran mayúsculas/minúsculas y espacios laterales) y la fecha de fin debe ser "
                    + "estrictamente posterior a la de inicio. Un taller nuevo no tiene participantes.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Taller creado",
                    content = @Content(schema = @Schema(implementation = TallerResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos",
                    content = @Content(mediaType = ApiDocs.PROBLEM_JSON,
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "El organizador no existe",
                    content = @Content(mediaType = ApiDocs.PROBLEM_JSON,
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Ya existe otro taller con ese título",
                    content = @Content(mediaType = ApiDocs.PROBLEM_JSON,
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<TallerResponseDTO> crear(@Valid @RequestBody TallerRequestDTO dto) {
        TallerResponseDTO creado = tallerService.crear(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.id()).toUri();
        return ResponseEntity.created(location).body(creado);
    }

    @GetMapping
    @Operation(summary = "Listar talleres",
            description = "Devuelve todos los talleres con su organizador y sus participantes. Lista vacía si no hay ninguno.")
    @ApiResponse(responseCode = "200", description = "Listado de talleres",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TallerResponseDTO.class))))
    public List<TallerResponseDTO> listar() {
        return tallerService.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un taller por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Taller encontrado",
                    content = @Content(schema = @Schema(implementation = TallerResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "El taller no existe",
                    content = @Content(mediaType = ApiDocs.PROBLEM_JSON,
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public TallerResponseDTO obtener(
            @Parameter(description = "Identificador del taller", example = "1") @PathVariable Long id) {
        return tallerService.obtener(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar un taller",
            description = "Sustituye título, fechas y organizador del taller. Conservar el título propio no genera "
                    + "conflicto; usar el de otro taller sí. Los participantes inscritos no se modifican.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Taller modificado",
                    content = @Content(schema = @Schema(implementation = TallerResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos",
                    content = @Content(mediaType = ApiDocs.PROBLEM_JSON,
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "El taller o el organizador no existen",
                    content = @Content(mediaType = ApiDocs.PROBLEM_JSON,
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Ya existe otro taller con ese título",
                    content = @Content(mediaType = ApiDocs.PROBLEM_JSON,
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public TallerResponseDTO actualizar(
            @Parameter(description = "Identificador del taller", example = "1") @PathVariable Long id,
            @Valid @RequestBody TallerRequestDTO dto) {
        return tallerService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un taller",
            description = "Elimina el taller y sus inscripciones. Los participantes y el organizador se conservan.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Taller eliminado"),
            @ApiResponse(responseCode = "404", description = "El taller no existe",
                    content = @Content(mediaType = ApiDocs.PROBLEM_JSON,
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Identificador del taller", example = "1") @PathVariable Long id) {
        tallerService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{tallerId}/participantes/{participanteId}")
    @Operation(summary = "Inscribir un participante en un taller",
            description = "Inscribe a un participante existente en un taller existente. No se permite repetir la misma inscripción.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Inscripción realizada; devuelve el taller actualizado",
                    content = @Content(schema = @Schema(implementation = TallerResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "El taller o el participante no existen",
                    content = @Content(mediaType = ApiDocs.PROBLEM_JSON,
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "El participante ya está inscrito en el taller",
                    content = @Content(mediaType = ApiDocs.PROBLEM_JSON,
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<TallerResponseDTO> inscribir(
            @Parameter(description = "Identificador del taller", example = "1") @PathVariable Long tallerId,
            @Parameter(description = "Identificador del participante", example = "1") @PathVariable Long participanteId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tallerService.inscribir(tallerId, participanteId));
    }

    @GetMapping("/{tallerId}/participantes")
    @Operation(summary = "Listar los participantes de un taller",
            description = "Devuelve los participantes inscritos en el taller. Si el taller existe pero no tiene inscritos, "
                    + "devuelve una lista vacía.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Participantes inscritos (posiblemente lista vacía)",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ParticipanteSimpleDTO.class)))),
            @ApiResponse(responseCode = "404", description = "El taller no existe",
                    content = @Content(mediaType = ApiDocs.PROBLEM_JSON,
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public List<ParticipanteSimpleDTO> listarParticipantes(
            @Parameter(description = "Identificador del taller", example = "1") @PathVariable Long tallerId) {
        return tallerService.listarParticipantes(tallerId);
    }

    @DeleteMapping("/{tallerId}/participantes/{participanteId}")
    @Operation(summary = "Eliminar una inscripción",
            description = "Elimina la inscripción conservando el taller y el participante. Si ambos existen pero no están "
                    + "vinculados, la operación finaliza sin cambios (204).")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Inscripción eliminada (o no existía)"),
            @ApiResponse(responseCode = "404", description = "El taller o el participante no existen",
                    content = @Content(mediaType = ApiDocs.PROBLEM_JSON,
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<Void> desinscribir(
            @Parameter(description = "Identificador del taller", example = "1") @PathVariable Long tallerId,
            @Parameter(description = "Identificador del participante", example = "1") @PathVariable Long participanteId) {
        tallerService.desinscribir(tallerId, participanteId);
        return ResponseEntity.noContent().build();
    }
}
