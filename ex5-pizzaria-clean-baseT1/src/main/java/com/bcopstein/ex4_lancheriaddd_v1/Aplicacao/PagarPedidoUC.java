package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ICozinhaService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.IPagamentoService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class PagarPedidoUC {
    private final PedidoService pedidoService;
    private final IPagamentoService pagamentoService;
    private final ICozinhaService cozinhaService;

    @Autowired
    public PagarPedidoUC(PedidoService pedidoService, 
                         IPagamentoService pagamentoService, 
                         ICozinhaService cozinhaService) {
        this.pedidoService = pedidoService;
        this.pagamentoService = pagamentoService;
        this.cozinhaService = cozinhaService;
    }

    public boolean run(long pedidoId) {
        // 1. Recupera o pedido
        Pedido pedido = pedidoService.recuperaPedido(pedidoId);
        if (pedido == null) return false;

        // 2. Tenta efetuar o pagamento no serviço fake
        if (pagamentoService.efetuarPagamento(pedido)) {
            // 3. Se passou, atualiza o status para PAGO e grava a data/hora
            Pedido pedidoPago = pedidoService.confirmarPagamento(pedidoId);
            
            // 4. O GRANDE MOMENTO: Manda o pedido para a Cozinha começar a trabalhar!
            cozinhaService.chegadaDePedido(pedidoPago);
            
            return true;
        }
        return false;
    }
}