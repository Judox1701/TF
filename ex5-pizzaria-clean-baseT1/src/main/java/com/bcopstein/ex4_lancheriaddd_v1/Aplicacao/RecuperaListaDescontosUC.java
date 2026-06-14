package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ServicoDesconto;

@Component
public class RecuperaListaDescontosUC {
    private final ServicoDesconto servicoDesconto;

    @Autowired
    public RecuperaListaDescontosUC(ServicoDesconto servicoDesconto) {
        this.servicoDesconto = servicoDesconto;
    }

    public List<String> run() {
        return servicoDesconto.politicasDisponiveis();
    }
}
