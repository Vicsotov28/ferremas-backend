package com.ferremas.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ferremas.backend.dto.SuscripcionRequest;
import com.ferremas.backend.dto.SuscripcionResponse;
import com.ferremas.backend.service.SuscripcionService;

@RestController
@RequestMapping("/api/suscripciones")
@CrossOrigin
public class SuscripcionController {

    @Autowired
    private SuscripcionService suscripcionService;

    @PostMapping
    public SuscripcionResponse registrarSuscripcion(
            @RequestBody SuscripcionRequest request) {
        return suscripcionService.registrarSuscripcion(request);
    }
}