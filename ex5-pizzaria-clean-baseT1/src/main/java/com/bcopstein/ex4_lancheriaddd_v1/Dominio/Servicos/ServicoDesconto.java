package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Desconto;

@Service
public class ServicoDesconto {

    private String descontoCorrente;
    private Map<String, Desconto> regra;
    
    public ServicoDesconto() {
        this.descontoCorrente = "1";
        this.regra = new HashMap<>();
        regra.put("1", new Desconto("1", valor -> valor * 0.07));
        regra.put("2", new Desconto("2", valor -> valor * 0.1));
    }

    public List<String> politicasDisponiveis() {
        return regra.keySet().stream().sorted().collect(Collectors.toList());
    }

    public void defineDescontoCorrente(String id) {
        if (!regra.containsKey(id)) {
            throw new IllegalArgumentException("Política de desconto não encontrada: " + id);
        }
        this.descontoCorrente = id;
    }

    public String getDescontoCorrente() {
        return descontoCorrente;
    }

    public double calcular(double valorDaVenda) {
        Desconto desconto = regra.get(this.descontoCorrente);
        if (desconto == null) {
            throw new IllegalArgumentException("Desconto não encontrado: " + this.descontoCorrente);
        }
        return desconto.calculaDesconto(valorDaVenda);
    }
}
