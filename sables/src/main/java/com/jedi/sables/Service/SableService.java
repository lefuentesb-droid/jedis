package com.jedi.sables.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jedi.sables.Dto.SableDTO;
import com.jedi.sables.Repository.SableRepository;
import com.jedi.sables.model.Sable;

@Service
public class SableService {

    @Autowired
    private SableRepository sableRepository;

    public List<SableDTO> obtenerTodos() {
        List<SableDTO> listaDTOs = new ArrayList<>();
        List<Sable> sablesReales = sableRepository.findAll();

        for (Sable s : sablesReales) {
            listaDTOs.add(convertirADTO(s));
        }

        return listaDTOs;
    }

    public SableDTO buscarPorId(Integer id) {
        Sable s = sableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sable no encontrado en los archivos Jedi"));

        return convertirADTO(s);
    }

    public SableDTO guardar(Sable nuevoSable) {
        Sable guardado = sableRepository.save(nuevoSable);
        return convertirADTO(guardado);
    }

    private SableDTO convertirADTO(Sable sable) {
        SableDTO dto = new SableDTO();
        dto.setId(sable.getId());
        dto.setColor(sable.getColor());
        dto.setCristalKyber(sable.getCristalKyber());
        dto.setJediId(sable.getJediId());
        return dto;
    }
}