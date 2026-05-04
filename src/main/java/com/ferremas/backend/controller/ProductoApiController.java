package com.ferremas.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ferremas.backend.model.Producto;
import com.ferremas.backend.service.ProductoService;


@RestController
@RequestMapping("/api/productos")
@CrossOrigin
public class ProductoApiController {
    
    @Autowired
    private ProductoService productoService;

    // API pública: listar productos disponibles
    @GetMapping
    public List<Producto> listarProductosApi() {
        return productoService.getAllProductos();
    }

    // API pública: consultar producto por código FERREMAS
    @GetMapping("/{codigoProducto}")
    public Producto buscarPorCodigo(@PathVariable String codigoProducto) {
        return productoService.getProductoByCodigo(codigoProducto);
    }

    // API pública: consultar productos por marca
    @GetMapping("/marca/{marca}")
    public List<Producto> buscarPorMarca(@PathVariable String marca) {
        return productoService.getProductosByMarca(marca);
    }
}
