package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ServicoDesconto;

@Component
public class RecuperaDescontoCorrenteUC {
    private final ServicoDesconto servicoDesconto;

    @Autowired
    public RecuperaDescontoCorrenteUC(ServicoDesconto servicoDesconto) {
        this.servicoDesconto = servicoDesconto;
    }

    public String run() {
        return servicoDesconto.getDescontoCorrente();
    }
}
