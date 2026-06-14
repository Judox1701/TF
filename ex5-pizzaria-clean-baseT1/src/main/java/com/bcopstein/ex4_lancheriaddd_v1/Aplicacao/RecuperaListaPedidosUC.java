package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class RecuperaListaPedidosUC {
    private final PedidoService pedidoService;

    @Autowired
    public RecuperaListaPedidosUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public List<Pedido> run() {
        return pedidoService.recuperaTodosPedidos();
    }
}
