package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class RecuperaStatusPedidoUC {
    private final PedidoService pedidoService;

    @Autowired
    public RecuperaStatusPedidoUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public Pedido run(long pedidoId) {
        return pedidoService.recuperaPedido(pedidoId);
    }
}
