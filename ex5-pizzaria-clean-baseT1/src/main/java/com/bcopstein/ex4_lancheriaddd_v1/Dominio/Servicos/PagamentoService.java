package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Service
public class PagamentoService implements IPagamentoService {
    @Override
    public boolean efetuarPagamento(Pedido pedido) {
        System.out.println("Pagamento processado com sucesso para o pedido: " + pedido.getId());
        return true;
    }
}