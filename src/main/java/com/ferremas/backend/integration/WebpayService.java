package com.ferremas.backend.integration;

import org.springframework.stereotype.Service;

@Service
public class WebpayService {

    public boolean procesarPago(Double monto, String metodoPago) {

        if (monto == null || monto <= 0) {
            return false;
        }

        if (metodoPago == null || metodoPago.isBlank()) {
            return false;
        }

        // Simulación de integración con Webpay
        // En una etapa futura, aquí se llamará a la API real de Transbank/Webpay.
        if (metodoPago.equalsIgnoreCase("TARJETA") ||
            metodoPago.equalsIgnoreCase("DEBITO") ||
            metodoPago.equalsIgnoreCase("CREDITO")) {
            return true;
        }

        return false;
    }
}