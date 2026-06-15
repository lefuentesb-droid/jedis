package com.jedi.jedis.Controller;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jedi.jedis.Dto.JediDTO;
import com.jedi.jedis.Service.JediService;
import com.jedi.jedis.model.Jedi;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/jedis")
@Tag(name = "Jedis", description = "Operaciones para gestionar guerreros Jedi")
public class JediController {


   @Autowired
   private JediService jediService;

    @Operation(
    summary = "Listar todos los jedis",
    description = "Obtiene todos los jedis registrados en la base de datos"
    )
    @GetMapping
    public ResponseEntity<List<JediDTO>> todas() {
        List<JediDTO> lista = jediService.obtenerTodos();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(
    summary = "Buscar jedi por ID",
    description = "Obtiene un jedi específico según su identificador"
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> porId(@PathVariable Integer id) {
        try {
            JediDTO dto = jediService.buscarPorId(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
    summary = "Registrar un nuevo jedi",
    description = "Crea un nuevo jedi con nombre, midiclorianos y bando"
    )
    @PostMapping
    public ResponseEntity<?> registrar(@Valid @RequestBody Jedi jedi) {
        try {
            JediDTO dto = jediService.guardar(jedi);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
