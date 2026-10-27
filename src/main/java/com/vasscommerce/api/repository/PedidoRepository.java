package com.vasscommerce.api.repository;

import com.vasscommerce.api.entity.Pedido;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @EntityGraph(attributePaths = {"itens", "itens.produto"})
    List<Pedido> findByClienteId(Long clienteId);
}
