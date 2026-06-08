package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public class ServicoImposto{
    public map<String,Imposto> impostos;

    public ServicoImposto(String lei){
        this.lei = lei;
        this.imposto = impostoAtual(lei);
    }
}
