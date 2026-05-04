package com.ferremas.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ferremas.backend.model.Producto;
import com.ferremas.backend.repository.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;
    
    // Obtener todos los productos
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    public Producto getProductoById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }

    // Guardar un producto
    public Producto saveProducto(Producto producto) {
    
    if (producto.getCodigoProducto() == null || producto.getCodigoProducto().isBlank()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El código del producto es obligatorio");
    }

    if (producto.getMarca() == null || producto.getMarca().isBlank()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La marca es obligatoria");
    }

    if (producto.getNombre() == null || producto.getNombre().isBlank()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del producto es obligatorio");
    }

    if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio debe ser mayor a 0");
    }

    if (producto.getStock() < 0) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El stock no puede ser negativo");
    }
    
    return productoRepository.save(producto);
}

    // Actualizar producto
    public Producto updateProducto(Long id, Producto productoActualizado) {

        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        if (productoActualizado.getNombre() == null || productoActualizado.getNombre().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del producto es obligatorio");
        }

        if (productoActualizado.getPrecio() == null || productoActualizado.getPrecio() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio debe ser mayor a 0");
        }

        if (productoActualizado.getStock() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El stock no puede ser negativo");
        }

       productoExistente.setCodigoProducto(productoActualizado.getCodigoProducto());
       productoExistente.setMarca(productoActualizado.getMarca());
       productoExistente.setCodigoMarca(productoActualizado.getCodigoMarca());
       productoExistente.setNombre(productoActualizado.getNombre());
       productoExistente.setModelo(productoActualizado.getModelo());
       productoExistente.setCategoria(productoActualizado.getCategoria());
       productoExistente.setDescripcion(productoActualizado.getDescripcion());
       productoExistente.setPrecio(productoActualizado.getPrecio());
       productoExistente.setStock(productoActualizado.getStock());

        return productoRepository.save(productoExistente);
    }

    // Eliminar producto
    public void deleteProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        productoRepository.delete(producto);
    }

    public Producto getProductoByCodigo(String codigoProducto) {
    return productoRepository.findByCodigoProducto(codigoProducto)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
}

    public List<Producto> getProductosByMarca(String marca) {
    List<Producto> productos = productoRepository.findByMarcaIgnoreCase(marca);

    if (productos.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron productos para la marca indicada");
    }

    return productos;
}
}