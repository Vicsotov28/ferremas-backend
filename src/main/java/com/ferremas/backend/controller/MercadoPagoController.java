package com.ferremas.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ferremas.backend.dto.MercadoPagoPreferenceRequest;
import com.ferremas.backend.dto.MercadoPagoPreferenceResponse;
import com.ferremas.backend.service.MercadoPagoService;

@RestController
@RequestMapping("/api/pagos/mercadopago")
@CrossOrigin
public class MercadoPagoController {

    @Autowired
    private MercadoPagoService mercadoPagoService;

    @PostMapping("/preferencia")
    public MercadoPagoPreferenceResponse crearPreferencia(
            @RequestBody MercadoPagoPreferenceRequest request) {
        return mercadoPagoService.crearPreferencia(request);
    }
}