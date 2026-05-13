package com.ferremas.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ferremas.backend.dto.DivisaResponse;
import com.ferremas.backend.service.DivisaService;

@RestController
@RequestMapping("/api/divisas")
@CrossOrigin
public class DivisaController {

    @Autowired
    private DivisaService divisaService;

    @GetMapping("/dolar")
    public DivisaResponse obtenerValorDolar() {
        return divisaService.obtenerValorDolar();
    }
}