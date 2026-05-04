package com.ferremas.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigoProducto; 
    private String marca;          
    private String codigoMarca;    
    private String nombre;
    private String modelo;
    private String categoria;
    private String descripcion;
    private Double precio;
    private int stock;
}
