package com.ferremas.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ferremas.backend.dto.PedidoRequest;
import com.ferremas.backend.dto.PagoRequest;

import com.ferremas.backend.model.Pedido;
import com.ferremas.backend.service.PedidoService;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin
public class PedidoController {
    
    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public List<Pedido> listarPedidos() {
        return pedidoService.listarPedidos();
    }

    @GetMapping("/{id}")
    public Pedido buscarPedidoPorId(@PathVariable Long id) {
        return pedidoService.buscarPedidoPorId(id);
    }

    @PostMapping
    public Pedido crearPedido(@RequestBody PedidoRequest request) {
    return pedidoService.crearPedido(
            request.getProductoId(),
            request.getUsuarioId(),
            request.getCantidad(),
            request.getTipoEntrega(),
            request.getDireccion()
    );
}
    @PutMapping("/pagar")
    public Pedido pagarPedido(@RequestBody PagoRequest request) {
    return pedidoService.procesarPago(
            request.getPedidoId(),
            request.getMetodoPago()
    );
}

    @PutMapping("/{id}/aprobar")
    public Pedido aprobarPedido(@PathVariable Long id) {
        return pedidoService.aprobarPedido(id);
    }

    @PutMapping("/{id}/rechazar")
    public Pedido rechazarPedido(@PathVariable Long id) {
        return pedidoService.rechazarPedido(id);
    } 

    @PutMapping("/{id}/preparar")
    public Pedido prepararPedido(@PathVariable Long id) {
        return pedidoService.prepararPedido(id);
    }

    @PutMapping("/{id}/listo-despacho")
    public Pedido marcarListoDespacho(@PathVariable Long id) {
        return pedidoService.marcarListoDespacho(id);
    }

    @PutMapping("/{id}/despachar")
    public Pedido despacharPedido(@PathVariable Long id) {
        return pedidoService.despacharPedido(id);
    }
}