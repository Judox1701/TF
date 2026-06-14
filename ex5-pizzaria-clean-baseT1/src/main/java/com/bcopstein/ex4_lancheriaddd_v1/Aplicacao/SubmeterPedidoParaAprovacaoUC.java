package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.PedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class SubmeterPedidoParaAprovacaoUC {
    private final PedidoService pedidoService;

    @Autowired
    public SubmeterPedidoParaAprovacaoUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public Pedido run(PedidoRequest pedidoRequest) {
        return pedidoService.submeterPedidoParaAprovacao(pedidoRequest.getClienteCpf(), pedidoRequest.getItens());
    }
}
