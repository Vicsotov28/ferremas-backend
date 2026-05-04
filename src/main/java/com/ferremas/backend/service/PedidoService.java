package com.ferremas.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import com.ferremas.backend.integration.WebpayService;

import com.ferremas.backend.model.*;
import com.ferremas.backend.repository.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private WebpayService webpayService;

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPedidoPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
    }

    public Pedido crearPedido(Long productoId, int cantidad, String tipoEntrega, String direccion) {

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        if (cantidad <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor a 0");
        }

        if (producto.getStock() < cantidad) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock insuficiente");
        }

        if (tipoEntrega == null || 
            (!tipoEntrega.equalsIgnoreCase("RETIRO") && !tipoEntrega.equalsIgnoreCase("DESPACHO"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de entrega inválido");
        }

        if (tipoEntrega.equalsIgnoreCase("DESPACHO") && (direccion == null || direccion.isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ingrese una dirección correcta para el despacho");
        }

        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);

        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDateTime.now());
        pedido.setProducto(producto);
        pedido.setCantidad(cantidad);
        pedido.setTotal(producto.getPrecio() * cantidad);
        pedido.setEstado("PENDIENTE");
        pedido.setTipoEntrega(tipoEntrega.toUpperCase());
        pedido.setDireccion(direccion);
        pedido.setMetodoPago("NO DEFINIDO");

        return pedidoRepository.save(pedido);
    }

   public Pedido procesarPago(Long pedidoId, String metodoPago) {

    Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));

    if (!pedido.getEstado().equalsIgnoreCase("PENDIENTE")) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El pedido ya ha sido procesado");
    }

    if (metodoPago == null || metodoPago.isBlank()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe ingresar un método de pago");
    }

    // Transferencia se valida como pago interno/manual por contador
    if (metodoPago.equalsIgnoreCase("TRANSFERENCIA")) {
        pedido.setEstado("PAGADO");
        pedido.setMetodoPago("TRANSFERENCIA");
        return pedidoRepository.save(pedido);
    }

    // Tarjeta / débito / crédito pasan por integración Webpay simulada
    boolean pagoAprobado = webpayService.procesarPago(pedido.getTotal(), metodoPago);

    if (pagoAprobado) {
        pedido.setEstado("PAGADO");
        pedido.setMetodoPago(metodoPago.toUpperCase());
    } else {
        pedido.setEstado("RECHAZADO");
        pedido.setMetodoPago(metodoPago.toUpperCase());
    }

    return pedidoRepository.save(pedido);
}

    public Pedido aprobarPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));

        if (!pedido.getEstado().equalsIgnoreCase("PAGADO")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se pueden aprobar pedidos pagados");
        }

        pedido.setEstado("APROBADO");
        return pedidoRepository.save(pedido);
    }

    public Pedido rechazarPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));

        if (!pedido.getEstado().equalsIgnoreCase("DESPACHADO")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede rechazar un pedido ya despachado");
        }

        pedido.setEstado("RECHAZADO");
        return pedidoRepository.save(pedido);
    }

    public Pedido prepararPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));

        if (!pedido.getEstado().equalsIgnoreCase("APROBADO")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se pueden preparar pedidos aprobados");
        }

        pedido.setEstado("EN_PREPARACION");
        return pedidoRepository.save(pedido);
    }

    public Pedido marcarListoDespacho(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));

        if (!pedido.getEstado().equalsIgnoreCase("EN_PREPARACION")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se pueden marcar como listos los pedidos en preparación");
        }

        pedido.setEstado("LISTO_DESPACHO");
        return pedidoRepository.save(pedido);
    }
    
    public Pedido despacharPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));

        if (!pedido.getEstado().equalsIgnoreCase("LISTO_DESPACHO")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se pueden despachar pedidos listos para despacho");
        }

        pedido.setEstado("DESPACHADO");
        return pedidoRepository.save(pedido);
    }
}