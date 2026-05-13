package com.ferremas.backend.service;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ferremas.backend.dto.DivisaResponse;

@Service
public class DivisaService {

    @Value("${bcentral.user:}")
    private String bcentralUser;

    @Value("${bcentral.pass:}")
    private String bcentralPass;

    @Value("${bcentral.serie.dolar:F073.TCO.PRE.Z.D}")
    private String serieDolar;

    public DivisaResponse obtenerValorDolar() {
        if (bcentralUser == null || bcentralUser.isBlank()
                || bcentralPass == null || bcentralPass.isBlank()) {
            return obtenerValorRespaldo("Credenciales Banco Central no configuradas");
        }

        try {
            RestTemplate restTemplate = new RestTemplate();

            LocalDate hoy = LocalDate.now();
            LocalDate desde = hoy.minusDays(20);

            URI uri = UriComponentsBuilder
                    .fromHttpUrl("https://si3.bcentral.cl/SieteRestWS/SieteRestWS.ashx")
                    .queryParam("user", bcentralUser)
                    .queryParam("pass", bcentralPass)
                    .queryParam("firstdate", desde.toString())
                    .queryParam("lastdate", hoy.toString())
                    .queryParam("timeseries", serieDolar)
                    .queryParam("function", "GetSeries")
                    .build()
                    .encode()
                    .toUri();

            Map<String, Object> respuesta = restTemplate.getForObject(uri, Map.class);

            if (respuesta == null || !respuesta.containsKey("Series")) {
                return obtenerValorRespaldo("Respuesta inválida desde Banco Central");
            }

            Map<String, Object> series = (Map<String, Object>) respuesta.get("Series");

            if (series == null || !series.containsKey("Obs")) {
                return obtenerValorRespaldo("Sin observaciones desde Banco Central");
            }

            List<Map<String, Object>> observaciones =
                    (List<Map<String, Object>>) series.get("Obs");

            if (observaciones == null || observaciones.isEmpty()) {
                return obtenerValorRespaldo("Serie sin datos recientes");
            }

            Map<String, Object> ultimaObservacionValida = null;

            for (int i = observaciones.size() - 1; i >= 0; i--) {
                Map<String, Object> obs = observaciones.get(i);

                Object valor = obs.get("value");
                Object estado = obs.get("statusCode");

                if (valor != null
                        && estado != null
                        && estado.toString().equalsIgnoreCase("OK")
                        && !valor.toString().equalsIgnoreCase("NaN")) {
                    ultimaObservacionValida = obs;
                    break;
                }
            }

            if (ultimaObservacionValida == null) {
                return obtenerValorRespaldo("No se encontró observación válida");
            }

            Double valorDolar = Double.valueOf(
                    ultimaObservacionValida.get("value").toString().replace(",", ".")
            );

            return new DivisaResponse(
                    "USD",
                    valorDolar,
                    "Banco Central de Chile - API BDE"
            );

        } catch (Exception e) {
            return obtenerValorRespaldo("Error al conectar con Banco Central");
        }
    }

    private DivisaResponse obtenerValorRespaldo(String motivo) {
        return new DivisaResponse(
                "USD",
                950.0,
                "Valor de respaldo: " + motivo
        );
    }
}