package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Repository
@Primary
public class PedidoRepositoryJPA implements PedidoRepository {

    private final PedidoSpringDataRepository jpaRepository;

    @Autowired
    public PedidoRepositoryJPA(PedidoSpringDataRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Pedido salva(Pedido pedido) {
        return jpaRepository.save(pedido);
    }

    @Override
    public Pedido atualiza(Pedido pedido) {
        return jpaRepository.save(pedido);
    }

    @Override
    public Pedido recuperaPedido(long id) {
        return jpaRepository.findById(id).orElse(null);
    }

    @Override
    public List<Pedido> recuperaTodos() {
        return jpaRepository.findAll();
    }

    @Override
    public List<Pedido> recuperaPedidosEntreguesEntreDatas(LocalDate inicio, LocalDate fim) {
        LocalDateTime dataInicio = inicio.atStartOfDay();
        LocalDateTime dataFim = fim.plusDays(1).atStartOfDay(); 
        
        return jpaRepository.buscarEntreguesEntreDatas(dataInicio, dataFim);
    }
}