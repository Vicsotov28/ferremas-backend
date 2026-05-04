package com.ferremas.backend.dto;

import lombok.Data;

@Data
public class PedidoRequest {
    
    private Long productoId;
    private int cantidad;
    private String tipoEntrega;
    private String direccion;
}
