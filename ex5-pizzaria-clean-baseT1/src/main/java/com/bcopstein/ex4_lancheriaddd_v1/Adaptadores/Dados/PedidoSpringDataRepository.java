package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public interface PedidoSpringDataRepository extends JpaRepository<Pedido, Long> {
    @Query("SELECT p FROM Pedido p WHERE p.status = 'ENTREGUE' AND p.dataHoraPagamento >= :inicio AND p.dataHoraPagamento < :fim")
    List<Pedido> buscarEntreguesEntreDatas(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
}