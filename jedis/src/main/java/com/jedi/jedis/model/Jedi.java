package com.jedi.jedis.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "jedis")

public class Jedi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NotBlank(message = "El nombre no puede quedar en el hiperespacio")
    @Size(min = 3, max = 50)
    private String nombre;

    private String bando;
    @NotNull(message = "El nivel de midiclorianos es obligatorio")
    @Min(100)
    private Integer midiclorianos;
    
        

}
