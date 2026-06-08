package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Imposto;

@Service
public class ServicoImposto {

    private String lei;
    private Map<String, Imposto> regra;
    
    @Autowired
    public ServicoImposto(@Value("${pizzaria.imposto.lei-vigente}") String lei) {
        this.lei = lei;

        this.regra = new HashMap<>();

        regra.put("0412/2022", new Imposto("0412/2022", valor -> valor * 0.10));
        regra.put("5762/2026", new Imposto("5762/2026", valor -> {
            if (valor <= 50.0) return 0.0;
            return valor * 0.15;
        }));
        
    }

    public double calcular(double valorDaVenda) {
        Imposto imposto = regra.get(lei);
        if (imposto == null) {
            throw new IllegalArgumentException("Lei não encontrada: " + lei);
        }
        return imposto.calculaImposto(valorDaVenda);
    }
}