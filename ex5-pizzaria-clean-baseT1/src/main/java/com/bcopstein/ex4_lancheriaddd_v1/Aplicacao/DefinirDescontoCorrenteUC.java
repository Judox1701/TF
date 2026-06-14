package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ServicoDesconto;

@Component
public class DefinirDescontoCorrenteUC {
    private final ServicoDesconto servicoDesconto;

    @Autowired
    public DefinirDescontoCorrenteUC(ServicoDesconto servicoDesconto) {
        this.servicoDesconto = servicoDesconto;
    }

    public void run(String id) {
        servicoDesconto.defineDescontoCorrente(id);
    }
}
