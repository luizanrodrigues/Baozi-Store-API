package com.baozistore.repository;

import com.baozistore.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    boolean existsByClienteId(Long clienteId);

    boolean existsByProdutoId(Long produtoId);
}
