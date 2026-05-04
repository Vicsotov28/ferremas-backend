package com.ferremas.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fecha;
    private Integer cantidad;
    private Double total;
    private String estado;
    private String tipoEntrega; // variable para indicar si el pedido es retirado o despachado
    private String direccion; 
    private String metodoPago;  // Tarjeta o transferencia 

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
}