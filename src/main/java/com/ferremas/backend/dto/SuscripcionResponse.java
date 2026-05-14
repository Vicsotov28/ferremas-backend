package com.ferremas.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuscripcionResponse {

    private Long id;
    private String email;
    private String mensaje;
}