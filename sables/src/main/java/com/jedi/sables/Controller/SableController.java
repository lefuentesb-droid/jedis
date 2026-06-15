package com.jedi.sables.Controller;

import java.util.List;

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
public class SableController {

    @Autowired
    private SableService sableService;

    @GetMapping
    public ResponseEntity<List<SableDTO>> todos() {
        List<SableDTO> lista = sableService.obtenerTodos();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> porId(@PathVariable Integer id) {
        try {
            SableDTO dto = sableService.buscarPorId(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

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