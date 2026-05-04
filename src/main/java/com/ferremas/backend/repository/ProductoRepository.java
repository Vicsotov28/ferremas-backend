package com.ferremas.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ferremas.backend.model.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByCodigoProducto(String codigoProducto);

    List<Producto> findByMarcaIgnoreCase(String marca);
}