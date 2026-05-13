package com.ferremas.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DivisaResponse {

    private String moneda;
    private Double valor;
    private String fuente;
}