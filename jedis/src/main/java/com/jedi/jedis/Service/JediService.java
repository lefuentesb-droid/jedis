package com.jedi.jedis.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jedi.jedis.Dto.JediDTO;
import com.jedi.jedis.Repository.JediRepository;
import com.jedi.jedis.model.Jedi;

@Service
public class JediService {


   @Autowired
   private JediRepository jediRepository;


   public List<JediDTO> obtenerTodos() {
       List<JediDTO> listaDTOs = new ArrayList<>();
       List<Jedi> jedisReales = jediRepository.findAll();
       for (Jedi j : jedisReales) {
           listaDTOs.add(convertirADTO(j));
       }
       return listaDTOs;
   }


   public JediDTO buscarPorId(Integer id) {
       Jedi j = jediRepository.findById(id)
           .orElseThrow(() -> new RuntimeException("Guerrero no encontrado en los archivos Jedi"));
       return convertirADTO(j);
   }


   public JediDTO guardar(Jedi nuevoJedi) {
       Jedi guardado = jediRepository.save(nuevoJedi);
       return convertirADTO(guardado);
   }


   private JediDTO convertirADTO(Jedi jedi) {
       JediDTO dto = new JediDTO();
       dto.setId(jedi.getId());
       dto.setNombre(jedi.getNombre());
       dto.setMidiclorianos(jedi.getMidiclorianos());
       dto.setBando(jedi.getBando());
       return dto;
   }
}
