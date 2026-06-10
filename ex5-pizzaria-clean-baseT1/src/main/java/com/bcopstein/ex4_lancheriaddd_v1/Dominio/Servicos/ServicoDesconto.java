package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Desconto;

@Service
public class ServicoDesconto {

    private String desconto;
    private Map<String, Desconto> regra;
    
    @Autowired
    public ServicoDesconto() {
        this.regra = new HashMap<>();
        regra.put("Desconto 7%", new Desconto("Desconto 7%", valor -> valor * 0.07));
                
    }

    public double calcular(double valorDaVenda) {
        Desconto desconto = regra.get(this.desconto);
        if (desconto == null) {
            throw new IllegalArgumentException("Desconto não encontrado: " + this.desconto);
        }
        return desconto.calculaDesconto(valorDaVenda);
    }
}
