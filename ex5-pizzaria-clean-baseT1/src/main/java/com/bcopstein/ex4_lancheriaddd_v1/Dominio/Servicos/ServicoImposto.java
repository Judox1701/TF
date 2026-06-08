package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.Map;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Imposto;

public class ServicoImposto {
    private final Map<String, Imposto> impostosRegistrados;

    public ServicoImposto(Map<String, Imposto> impostosRegistrados) {
        this.impostosRegistrados = impostosRegistrados;
    }

    public double calcular(double valorDaVenda, String leiAtiva) {
        Imposto imposto = impostosRegistrados.get(leiAtiva);
        if (imposto == null) {
            throw new IllegalArgumentException("Lei não encontrada: " + leiAtiva);
        }
        return imposto.calculaImposto(valorDaVenda);
    }
}