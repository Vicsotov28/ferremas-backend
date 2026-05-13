package com.ferremas.backend.dto;

import lombok.Data;

@Data
public class MercadoPagoPreferenceRequest {

    private Long pedidoId;
    private String titulo;
    private Integer cantidad;
    private Double precioUnitario;
}