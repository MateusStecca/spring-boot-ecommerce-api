package com.mateus.ecommerce_api.repository;

import com.mateus.ecommerce_api.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Procura todos os pedidos associados ao email do utilizador
    List<Pedido> findByEmailUsuario(String emailUsuario);
}