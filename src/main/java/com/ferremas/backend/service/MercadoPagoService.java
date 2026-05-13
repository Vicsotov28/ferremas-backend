package com.ferremas.backend.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ferremas.backend.dto.MercadoPagoPreferenceRequest;
import com.ferremas.backend.dto.MercadoPagoPreferenceResponse;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;

@Service
public class MercadoPagoService {

    @Value("${mercadopago.access.token:}")
    private String accessToken;

    public MercadoPagoPreferenceResponse crearPreferencia(
            MercadoPagoPreferenceRequest request) {

        if (accessToken == null || accessToken.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Access Token de Mercado Pago no configurado"
            );
        }

        if (request.getPedidoId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El ID del pedido es obligatorio"
            );
        }

        if (request.getTitulo() == null || request.getTitulo().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El título del producto es obligatorio"
            );
        }

        if (request.getCantidad() == null || request.getCantidad() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La cantidad debe ser mayor a 0"
            );
        }

        if (request.getPrecioUnitario() == null || request.getPrecioUnitario() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El precio unitario debe ser mayor a 0"
            );
        }

        try {
            MercadoPagoConfig.setAccessToken(accessToken);

            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .id(request.getPedidoId().toString())
                    .title(request.getTitulo())
                    .description("Pedido FERREMAS #" + request.getPedidoId())
                    .quantity(request.getCantidad())
                    .currencyId("CLP")
                    .unitPrice(BigDecimal.valueOf(request.getPrecioUnitario()))
                    .build();

            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(item);

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .externalReference("PEDIDO_" + request.getPedidoId())
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            return new MercadoPagoPreferenceResponse(
                    preference.getId(),
                    preference.getInitPoint(),
                    preference.getSandboxInitPoint()
            );

        } catch (Exception e) {
            e.printStackTrace();

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error Mercado Pago: " + e.getMessage()
            );
        }
    }
}