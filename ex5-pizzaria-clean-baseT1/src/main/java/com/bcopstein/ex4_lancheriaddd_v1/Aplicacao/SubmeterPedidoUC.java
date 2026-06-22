package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.PedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Service
public class SubmeterPedidoUC {
    private final PedidoService pedidoService;

    @Autowired
    public SubmeterPedidoUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public Pedido run(PedidoRequest pedidoRequest) {
        return pedidoService.submeterPedidoParaAprovacao(pedidoRequest.getClienteCpf(), pedidoRequest.getItens());
    }
}

