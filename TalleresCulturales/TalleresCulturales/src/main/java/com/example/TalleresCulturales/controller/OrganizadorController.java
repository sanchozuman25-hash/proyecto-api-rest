package com.example.TalleresCulturales.controller;

import com.example.TalleresCulturales.config.ApiDocs;
import com.example.TalleresCulturales.dto.OrganizadorRequestDTO;
import com.example.TalleresCulturales.dto.OrganizadorResponseDTO;
import com.example.TalleresCulturales.service.OrganizadorService;
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
@RequestMapping("/organizadores")
@RequiredArgsConstructor
@Tag(name = "Organizadores", description = "Gestión de los organizadores de talleres (CRUD completo)")
public class OrganizadorController {

    private final OrganizadorService organizadorService;

    @PostMapping
    @Operation(summary = "Crear un organizador",
            description = "Crea un nuevo organizador. Devuelve 201 con la cabecera Location apuntando al recurso creado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Organizador creado",
                    content = @Content(schema = @Schema(implementation = OrganizadorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos",
                    content = @Content(mediaType = ApiDocs.PROBLEM_JSON,
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<OrganizadorResponseDTO> crear(@Valid @RequestBody OrganizadorRequestDTO dto) {
        OrganizadorResponseDTO creado = organizadorService.crear(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.id()).toUri();
        return ResponseEntity.created(location).body(creado);
    }

    @GetMapping
    @Operation(summary = "Listar organizadores",
            description = "Devuelve todos los organizadores con un resumen de sus talleres. Lista vacía si no hay ninguno.")
    @ApiResponse(responseCode = "200", description = "Listado de organizadores",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrganizadorResponseDTO.class))))
    public List<OrganizadorResponseDTO> listar() {
        return organizadorService.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un organizador por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Organizador encontrado",
                    content = @Content(schema = @Schema(implementation = OrganizadorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "El organizador no existe",
                    content = @Content(mediaType = ApiDocs.PROBLEM_JSON,
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public OrganizadorResponseDTO obtener(
            @Parameter(description = "Identificador del organizador", example = "1") @PathVariable Long id) {
        return organizadorService.obtener(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar un organizador",
            description = "Sustituye el nombre y el email del organizador indicado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Organizador modificado",
                    content = @Content(schema = @Schema(implementation = OrganizadorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos",
                    content = @Content(mediaType = ApiDocs.PROBLEM_JSON,
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "El organizador no existe",
                    content = @Content(mediaType = ApiDocs.PROBLEM_JSON,
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public OrganizadorResponseDTO actualizar(
            @Parameter(description = "Identificador del organizador", example = "1") @PathVariable Long id,
            @Valid @RequestBody OrganizadorRequestDTO dto) {
        return organizadorService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un organizador",
            description = "Elimina el organizador. No se permite si todavía tiene talleres asociados.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Organizador eliminado"),
            @ApiResponse(responseCode = "404", description = "El organizador no existe",
                    content = @Content(mediaType = ApiDocs.PROBLEM_JSON,
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "El organizador tiene talleres asociados",
                    content = @Content(mediaType = ApiDocs.PROBLEM_JSON,
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Identificador del organizador", example = "1") @PathVariable Long id) {
        organizadorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
