package com.ferremas.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MercadoPagoPreferenceResponse {

    private String preferenceId;
    private String initPoint;
    private String sandboxInitPoint;
}