package com.jedi.sables.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sables")
public class Sable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El color del sable es obligatorio")
    private String color;

    @NotBlank(message = "El cristal kyber es obligatorio")
    @Column(name = "cristal_kyber")
    private String cristalKyber;

    @NotNull(message = "El ID del Jedi es obligatorio")
    @Column(name = "jedi_id")
    private Integer jediId;
}