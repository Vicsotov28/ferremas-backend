package com.ferremas.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ferremas.backend.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
