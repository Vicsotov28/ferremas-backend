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
    private String tipoEntrega; // RETIRO o DESPACHO
    private String direccion; // solo si es despacho
    private String metodoPago; // TARJETA, DEBITO, CREDITO, TRANSFERENCIA, NO DEFINIDO

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}