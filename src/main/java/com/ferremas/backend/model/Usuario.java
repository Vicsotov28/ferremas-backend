package com.ferremas.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    private String rol; // CLIENTE, ADMINISTRADOR, VENDEDOR, BODEGUERO, CONTADOR
}