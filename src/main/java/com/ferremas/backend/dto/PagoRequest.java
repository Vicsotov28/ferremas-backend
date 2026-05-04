package com.ferremas.backend.dto;

import lombok.Data;

@Data
public class PagoRequest {
    
    private Long pedidoId;
    private String metodoPago;
}
