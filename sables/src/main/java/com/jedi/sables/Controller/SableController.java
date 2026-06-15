package com.jedi.sables.Controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jedi.sables.Dto.SableDTO;
import com.jedi.sables.Service.SableService;
import com.jedi.sables.model.Sable;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/sables")
@Tag(name = "Sables", description = "Operaciones para gestionar sables láser")
public class SableController {

    @Autowired
    private SableService sableService;

    @Operation(
        summary = "Listar todos los sables",
        description = "Obtiene todos los sables registrados en la base de datos"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de sables obtenida correctamente",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                [
                  {
                    "id": 1,
                    "color": "Verde",
                    "cristalKyber": "Ilum",
                    "jediId": 1
                  },
                  {
                    "id": 2,
                    "color": "Azul",
                    "cristalKyber": "Ilum",
                    "jediId": 2
                  }
                ]
                """
            )
        )
    )
    @GetMapping
    public ResponseEntity<List<SableDTO>> todos() {
        List<SableDTO> lista = sableService.obtenerTodos();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(
        summary = "Buscar sable por ID",
        description = "Obtiene un sable específico según su identificador"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Sable encontrado correctamente",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "id": 2,
                  "color": "Azul",
                  "cristalKyber": "Ilum",
                  "jediId": 2
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Sable no encontrado"
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> porId(@PathVariable Integer id) {
        try {
            SableDTO dto = sableService.buscarPorId(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
        summary = "Registrar un nuevo sable",
        description = "Crea un nuevo sable láser con color, cristal kyber y el ID del jedi asociado"
    )
    @ApiResponse(
        responseCode = "201",
        description = "Sable creado correctamente",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "id": 3,
                  "color": "Morado",
                  "cristalKyber": "Adegan",
                  "jediId": 2
                }
                """
            )
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Datos inválidos para crear el sable"
    )
    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody Sable sable) {
        try {
            SableDTO dto = sableService.guardar(sable);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}