package com.ferremas.backend.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ferremas.backend.dto.SuscripcionRequest;
import com.ferremas.backend.dto.SuscripcionResponse;
import com.ferremas.backend.model.Suscripcion;
import com.ferremas.backend.repository.SuscripcionRepository;

@Service
public class SuscripcionService {

    @Autowired
    private SuscripcionRepository suscripcionRepository;

    public SuscripcionResponse registrarSuscripcion(SuscripcionRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debe ingresar un correo electrónico"
            );
        }

        String email = request.getEmail().trim().toLowerCase();

        if (!email.contains("@") || !email.contains(".")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ingrese un correo electrónico válido"
            );
        }

        if (suscripcionRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El correo ya se encuentra suscrito"
            );
        }

        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setEmail(email);
        suscripcion.setFechaRegistro(LocalDateTime.now());

        Suscripcion guardada = suscripcionRepository.save(suscripcion);

        return new SuscripcionResponse(
                guardada.getId(),
                guardada.getEmail(),
                "¡Felicitaciones! Tu correo fue registrado correctamente para recibir ofertas y descuentos FERREMAS."
        );
    }
}