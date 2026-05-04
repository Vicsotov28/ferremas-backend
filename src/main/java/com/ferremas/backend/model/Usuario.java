package com.ferremas.backend.model;

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

    private String password;
    private String rol; // Cliente, administrador, vendedor, bodeguero, contador
}
